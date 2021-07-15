package software.bernie.techarium.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class TechariumEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {
    public TechariumEnergyStorage(int capacity) {
        super(capacity);
    }

    public TechariumEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public TechariumEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public TechariumEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("energy", this.energy);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
    }

    public TechariumEnergyStorage copy() {
        return new TechariumEnergyStorage(capacity, maxReceive, maxExtract, energy);
    }
}
