package software.bernie.techarium.tile.sync;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidStackDataSlot extends TechariumDataSlot<FluidStack> {
    public FluidStackDataSlot(Supplier<FluidStack> getter, Consumer<FluidStack> setter) {
        super(getter, setter);
    }

    public FluidStackDataSlot(Supplier<FluidStack> getter, Consumer<FluidStack> setter, SyncMode mode) {
        super(getter, setter, mode);
    }

    @Override
    public CompoundNBT toNBT() {
        return getter.get().writeToNBT(new CompoundNBT());
    }

    @Override
    public void fromNBT(CompoundNBT nbt) {
        setter.accept(FluidStack.loadFluidStackFromNBT(nbt));
    }

    @Override
    public void updatePrevValue() {
        prevValue = getter.get() == null ? null : getter.get().copy();
    }

    @Override
    protected boolean areEqual() {
        FluidStack newFluid = getter.get();
        return prevValue.isFluidStackIdentical(newFluid);
    }
}
