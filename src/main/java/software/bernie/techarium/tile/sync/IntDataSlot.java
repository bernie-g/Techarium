package software.bernie.techarium.tile.sync;

import net.minecraft.nbt.CompoundNBT;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntDataSlot extends TechariumDataSlot<Integer> {
    public IntDataSlot(Supplier<Integer> getter, Consumer<Integer> setter) {
        super(getter, setter);
    }

    public IntDataSlot(Supplier<Integer> getter, Consumer<Integer> setter, SyncMode mode) {
        super(getter, setter, mode);
    }
    @Override
    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("int", getter.get());
        return nbt;
    }

    @Override
    public void fromNBT(CompoundNBT nbt) {
        setter.accept(nbt.getInt("int"));
    }
}
