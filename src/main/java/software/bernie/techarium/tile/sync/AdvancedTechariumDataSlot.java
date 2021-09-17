package software.bernie.techarium.tile.sync;

import net.minecraft.nbt.CompoundNBT;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AdvancedTechariumDataSlot<T extends TechariumSerializable<T> & Comparable<T>> extends TechariumDataSlot<T> {

    public AdvancedTechariumDataSlot(Supplier<T> getter, Consumer<T> setter) {
        super(getter, setter);
    }

    @Override
    protected boolean areEqual() {
        return prevValue.compareTo(getter.get()) != 0;
    }

    @Override
    public CompoundNBT toNBT() {
        return getter.get().serializeNBT();
    }

    @Override
    public void fromNBT(CompoundNBT nbt) {
        setter.accept(getter.get().deserializeNBT(nbt));
    }

    @Override
    public void updatePrevValue() {
        prevValue = getter.get() == null ? null : getter.get().copy();
    }
}
