package software.bernie.techarium.tile.sync;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ResourceLocationDataSlot extends TechariumDataSlot<ResourceLocation> {
    public ResourceLocationDataSlot(Supplier<ResourceLocation> getter, Consumer<ResourceLocation> setter) {
        super(getter, setter);
    }

    public ResourceLocationDataSlot(Supplier<ResourceLocation> getter, Consumer<ResourceLocation> setter, SyncMode mode) {
        super(getter, setter, mode);
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ResourceLocation rl = getter.get();
        if (rl != null)
            nbt.putString("rl", rl.toString());
        return nbt;
    }

    @Override
    protected boolean areEqual() {
        return prevValue.toString().equals(getter.get().toString());
    }

    @Override
    public void fromNBT(CompoundNBT nbt) {
        if (nbt.contains("rl"))
            setter.accept(nbt.contains("rl") ? new ResourceLocation(nbt.getString("rl")) : null);
    }
}
