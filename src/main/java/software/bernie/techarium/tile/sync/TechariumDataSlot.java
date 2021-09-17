package software.bernie.techarium.tile.sync;

import lombok.Getter;
import net.minecraft.nbt.CompoundNBT;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class TechariumDataSlot<T> {
    final Supplier<T> getter;
    final Consumer<T> setter;
    @Getter
    final SyncMode mode;
    T prevValue;

    public TechariumDataSlot(Supplier<T> getter, Consumer<T> setter) {
        this(getter, setter, SyncMode.GUI);
    }
    public TechariumDataSlot(Supplier<T> getter, Consumer<T> setter, SyncMode mode) {
        this.getter = getter;
        this.setter = setter;
        this.mode = mode;
    }
    public Optional<CompoundNBT> toOptionalNBT() {
        if (needsUpdate()) {
            return Optional.of(toNBT());
        }
        return Optional.empty();
    }

    public abstract CompoundNBT toNBT();

    public abstract void fromNBT(CompoundNBT nbt);

    public void updatePrevValue() {
        prevValue = getter.get();
    }
    public boolean needsUpdate() {
        if (prevValue == null)
            return getter.get() != null;
        if (getter.get() == null)
            return false;
        return !areEqual();
    }

    protected boolean areEqual() {
        return prevValue.equals(getter.get());
    }
    public enum SyncMode {
        RENDER,
        GUI;
    }
}
