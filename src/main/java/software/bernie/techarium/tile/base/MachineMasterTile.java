package software.bernie.techarium.tile.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
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
import software.bernie.techarium.machine.controller.MultiController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static software.bernie.techarium.util.StaticHandler.getSideFromDirection;

public abstract class MachineMasterTile extends MachineTileBase implements INamedContainerProvider, ITickableTileEntity {

    private MultiController controller;

    public MachineMasterTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.controller = new MultiController();
    }

    public MultiController getController() {
        return controller;
    }

    public MachineController getActiveController(){
        return getController().getActiveController();
    }

    public boolean isPowered(){
        return getActiveController().isPowered();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey() + "_" + getController().getActiveTier()).applyTextStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        return new AutomaticContainer(this,inv,id,getDisplayName());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY || cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (world != null && getFaceConfigs().get(getSideFromDirection(side,getFacingDirection())).allowsConnection()) {
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
        } else if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return getActiveController().getMultiInventory().getInvOptional().cast();
        } else if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
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
    public void tick() {
        this.getActiveController().tick();
    }
}
