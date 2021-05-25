package software.bernie.techarium.pipes.networks;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import software.bernie.techarium.pipes.capability.PipeType;

public class EnergyPipeNetwork extends PipeNetwork<IEnergyStorage, Integer> {
    @Override
    public boolean isType(PipeType type) {
        return type == PipeType.ENERGY;
    }

    @Override
    public void tick() {

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
    public Integer fill(IEnergyStorage capability, Integer energy, boolean simulate) {
        return capability.receiveEnergy(energy, simulate);
    }

    @Override
    public int getSlots(IEnergyStorage capability) {
        return 1;
    }
}
