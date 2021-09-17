package software.bernie.techarium.tile.sync;

import net.minecraft.nbt.CompoundNBT;

public interface TechariumSerializable<T extends TechariumSerializable<T>> {
        CompoundNBT serializeNBT();
        T deserializeNBT(CompoundNBT nbt);
        T copy();
}
