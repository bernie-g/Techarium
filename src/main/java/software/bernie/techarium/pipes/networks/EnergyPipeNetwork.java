package software.bernie.techarium.pipes.networks;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.capability.PipeType;

public class EnergyPipeNetwork extends PipeNetwork<IEnergyStorage, Integer> {

    @Override
    public PipeType getType() {
        return PipeType.ENERGY;
    }

    @Override
    public Filter<Integer> getFilter(PipePosition pipePosition) {
        return new Filter<Integer>(){};
    }

    @Override
    public boolean isEmpty(Integer integer) {
        return integer <= 0;
    }

    @Override
    public int getMaxRemove() {
        return 1000;
    }

    @Override
    public Capability<IEnergyStorage> getDefaultCapability() {
        return CapabilityEnergy.ENERGY;
    }

    @Override
    public boolean canFill(IEnergyStorage capability, Integer integer) {
        return !fill(capability, integer, true).equals(integer);
    }

    @Override
    public Integer drain(IEnergyStorage capability, int amount, int slot, boolean simulate) {
        return capability.extractEnergy(amount, simulate);
    }

    @Override
    public Integer drainWith(IEnergyStorage capability, Integer drain, int slot, boolean simulate) {
        return capability.extractEnergy(drain, simulate);
    }

    @Override
    public Integer fill(IEnergyStorage capability, Integer energy, boolean simulate) {
        return capability.receiveEnergy(energy, simulate);
    }

    @Override
    public int getSlots(IEnergyStorage capability) {
        return 1;
    }
}
