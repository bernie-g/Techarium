package software.bernie.techarium.tile.sync;

import net.minecraft.nbt.CompoundNBT;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BooleanDataSlot extends TechariumDataSlot<Boolean> {
    public BooleanDataSlot(Supplier<Boolean> getter, Consumer<Boolean> setter) {
        super(getter, setter);
    }

    public BooleanDataSlot(Supplier<Boolean> getter, Consumer<Boolean> setter, SyncMode mode) {
        super(getter, setter, mode);
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("boolean", getter.get());
        return nbt;
    }

    @Override
    public void fromNBT(CompoundNBT nbt) {
        setter.accept(nbt.getBoolean("boolean"));
    }
}
