package software.bernie.techarium.tile.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import software.bernie.techarium.machine.container.AutomaticContainer;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.controller.MultiController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MachineTile extends TileEntity implements INamedContainerProvider {

    private MultiController controller;

    public MachineTile(TileEntityType<?> tileEntityTypeIn) {
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
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY && isPowered()) {
            return getActiveController().getLazyEnergyStorage().cast();
        } else if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){

        }
        return super.getCapability(cap, side);
    }

}
