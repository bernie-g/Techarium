package software.bernie.techarium.pipes.networks;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import software.bernie.techarium.pipes.capability.PipeType;

public class FluidPipeNetwork extends PipeNetwork<IFluidHandler, FluidStack> {

    @Override
    public boolean isType(PipeType type) {
        return type == PipeType.FLUID;
    }

    @Override
    public void tick() {

    }

    @Override
    public Capability<IFluidHandler> getDefaultCapability() {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
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
    public FluidStack fill(IFluidHandler capability, FluidStack fluidStack, boolean simulate) {
        return new FluidStack(fluidStack.getFluid(), capability.fill(fluidStack, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    @Override
    public int getSlots(IFluidHandler capability) {
        return capability.getTanks();
    }
}
