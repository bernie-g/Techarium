package software.bernie.techarium.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

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

    public void outputToSide(World world, BlockPos pos, Direction side, int max) {
        TileEntity te = world.getBlockEntity(pos.relative(side));
        if(te == null)
            return;
        LazyOptional<IEnergyStorage> opt = te.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
        IEnergyStorage ies = opt.orElse(null);
        if (ies == null)
            return;

        int maxPush = ies.receiveEnergy(this.extractEnergy(max, true), true);
        ies.receiveEnergy(maxPush, false);
        this.extractEnergy(maxPush, false);
    }

    public void inputFromSide(World world, BlockPos pos, Direction side, int max) {
        TileEntity te = world.getBlockEntity(pos.relative(side));
        if (te == null)
            return;
        LazyOptional<IEnergyStorage> opt = te.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
        IEnergyStorage ies = opt.orElse(null);
        if (ies == null)
            return;

        int pullMax = this.receiveEnergy(ies.extractEnergy(max, true), true);
        ies.extractEnergy(pullMax, false);
        ies.receiveEnergy(pullMax, false);
    }
    
    public TechariumEnergyStorage copy() {
        return new TechariumEnergyStorage(capacity, maxReceive, maxExtract, energy);
    }

    public float getPercentageFull() {
        return (float) getEnergyStored() / getMaxEnergyStored();
    }
}
