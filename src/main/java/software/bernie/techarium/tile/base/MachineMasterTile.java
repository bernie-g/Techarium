package software.bernie.techarium.tile.base;

import mekanism.common.util.InventoryUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import software.bernie.techarium.machine.container.AutomaticContainer;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.controller.MultiController;
import software.bernie.techarium.machine.interfaces.recipe.IForcedRecipe;
import software.bernie.techarium.machine.interfaces.recipe.IMachineRecipe;
import software.bernie.techarium.machine.interfaces.recipe.IRecipeMachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

import static software.bernie.techarium.util.StaticHandler.*;

public abstract class MachineMasterTile<T extends IMachineRecipe> extends MachineTileBase implements INamedContainerProvider, ITickableTileEntity, IRecipeMachine<T>, IForcedRecipe {

    private final MultiController controller;

    public MachineMasterTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.controller = new MultiController();
    }

    public MultiController getController() {
        return controller;
    }

    public MachineController<T> getActiveController() {
        return (MachineController<T>) getController().getActiveController();
    }

    public boolean isPowered() {
        return getActiveController().isPowered();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey() + "_" + getController().getActiveTier()).applyTextStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        return new AutomaticContainer(this, inv, id, getDisplayName());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY || cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (world != null && getFaceConfigs().get(getSideFromDirection(side, getFacingDirection())).allowsConnection()) {
                return this.getCapability(cap);
            }
        }
        return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap) {
        if (cap == CapabilityEnergy.ENERGY && isPowered()) {
            return getActiveController().getLazyEnergyStorage().cast();
        } else if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return getActiveController().getMultiInventory().getInvOptional().cast();
        } else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return getActiveController().getMultiTank().getTankOptional().cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public ActionResultType onTileActicated(PlayerEntity player) {
        NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer -> {
            packetBuffer.writeBlockPos(this.getPos());
            packetBuffer.writeTextComponent(this.getDisplayName());
        });
        return ActionResultType.SUCCESS;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    private boolean isFirstLoad = true;

    @Override
    public void tick() {
        this.getActiveController().tick();
        if (!world.isRemote()) {
            if(isFirstLoad) {
                updateMachineTile();
                isFirstLoad = false;
            } else if(world.getGameTime() % 3 == 0){
                isFirstLoad = true;
            }
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        getActiveController().deserializeNBT(compound.getCompound("activeMachine"));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("activeMachine", getActiveController().serializeNBT());
        return super.write(compound);
    }

    protected void updateMachineTile() {
        requestModelDataUpdate();
        this.markDirty();
        if (this.getWorld() != null) {
            this.getWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.deserializeNBT(tag);
        updateMachineTile();
    }

    public void masterHandleDestruction(){
        getActiveController().getMultiInventory().getInvOptional().ifPresent(multiInv -> multiInv.getInventories().forEach(inv -> getItemStackStream(inv).forEach(stack -> spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack,world.rand))));
    }

}
