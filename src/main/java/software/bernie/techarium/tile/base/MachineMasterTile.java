package software.bernie.techarium.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import software.bernie.techarium.machine.container.AutomaticContainer;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.interfaces.recipe.IForcedRecipe;
import software.bernie.techarium.machine.interfaces.recipe.IMachineRecipe;
import software.bernie.techarium.machine.interfaces.recipe.IRecipeMachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static software.bernie.techarium.util.StaticHandler.*;

public abstract class MachineMasterTile<T extends IMachineRecipe> extends MachineTileBase implements INamedContainerProvider, ITickableTileEntity, IRecipeMachine<T>, IForcedRecipe {

    private final MachineController<T> controller;

    public MachineMasterTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        controller = createMachineController();
    }

    public MachineController<T> getController() {
        return controller;
    }

    public boolean isPowered() {
        return getController().isPowered();
    }

    @Override
    public ITextComponent getDisplayName() {
        TranslationTextComponent component = new TranslationTextComponent(this.getBlockState().getBlock().getDescriptionId());
        component.getStyle().applyFormat(TextFormatting.BLACK);
        return component;
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
            if (level != null && getFaceConfigs().get(getSideFromDirection(side, getFacingDirection())).allowsConnection()) {
                return this.getCapability(cap);
            }
        }
        return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap) {
        if (cap == CapabilityEnergy.ENERGY && isPowered()) {
            return getController().getLazyEnergyStorage().cast();
        } else if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return getController().getMultiInventory().getInvOptional().cast();
        } else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return getController().getMultiTank().getTankOptional().cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public ActionResultType onTileActivated(PlayerEntity player) {
        NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer -> {
            packetBuffer.writeBlockPos(this.getBlockPos());
            packetBuffer.writeComponent(this.getDisplayName());
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
        this.getController().tick();
        if (!level.isClientSide()) {
            if(isFirstLoad) {
                updateMachineTile();
                isFirstLoad = false;
            } else if(level.getGameTime() % 3 == 0){
                isFirstLoad = true;
            }
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        getController().deserializeNBT(nbt.getCompound("activeMachine"));
        super.load(state, nbt);
        updateMachineTile();
    }


    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("activeMachine", getController().serializeNBT());
        return super.save(compound);
    }

    protected void updateMachineTile() {
        requestModelDataUpdate();
        this.setChanged();
        if (this.getLevel() != null) {
            this.getLevel().sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(this.getBlockState(), pkt.getTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.serializeNBT();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.deserializeNBT(tag);
        updateMachineTile();
    }

    public void masterHandleDestruction(){
        getController().getMultiInventory().getInvOptional().ifPresent(multiInv -> multiInv.getInventories().forEach(inv -> getItemStackStream(inv).forEach(stack -> spawnItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack,level.random))));
    }

    protected abstract MachineController<T> createMachineController();
}
