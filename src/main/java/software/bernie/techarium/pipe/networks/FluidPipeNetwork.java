package software.bernie.techarium.pipe.networks;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import software.bernie.techarium.pipe.PipePosition;
import software.bernie.techarium.pipe.util.PipeType;

public class FluidPipeNetwork extends PipeNetwork<IFluidHandler, FluidStack> {

    @Override
    public PipeType getType() {
        return PipeType.FLUID;
    }

    @Override
    public boolean isEmpty(FluidStack fluidStack) {
        return fluidStack.isEmpty();
    }

    @Override
    public Capability<IFluidHandler> getDefaultCapability() {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public int getMaxRemove() {
        return 100;
    }

    @Override
    public boolean canFill(IFluidHandler capability, FluidStack fluidStack) {
        return fill(capability, fluidStack, true).getAmount() == fluidStack.getAmount();
    }

    @Override
    public FluidStack drain(IFluidHandler capability, int amount, int slot, boolean simulate) {
        return capability.drain(new FluidStack(capability.getFluidInTank(slot), amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public FluidStack drainWith(IFluidHandler capability, FluidStack drain, int slot, boolean simulate) {
        return capability.drain(drain, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public FluidStack fill(IFluidHandler capability, FluidStack fluidStack, boolean simulate) {
        return new FluidStack(fluidStack.getFluid(), capability.fill(fluidStack, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    @Override
    public int getSlots(IFluidHandler capability) {
        return capability.getTanks();
    }
}
