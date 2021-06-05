package software.bernie.techarium.machine.addon.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class MultiTankCapHandler implements IFluidHandler {

    private final List<FluidTankAddon> tanks;

    public MultiTankCapHandler(List<FluidTankAddon> tanks){
        this.tanks = tanks;
    }

    public boolean isEmpty() {
        return this.tanks.isEmpty();
    }

    public List<FluidTankAddon> getFluidTanks(){
        return tanks;
    }

    @Override
    public int getTanks() {
        return this.tanks.size();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.tanks.get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.tanks.get(tank).getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return this.tanks.get(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        Iterator tanks = this.tanks.iterator();

        FluidTankAddon tank;
        do {
            if (!tanks.hasNext()) {
                return 0;
            }

            tank = (FluidTankAddon) tanks.next();
        } while(tank.fill(resource, FluidAction.SIMULATE) == 0);

        return tank.fill(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        Iterator tanks = this.tanks.iterator();

        FluidTankAddon tank;
        do {
            if (!tanks.hasNext()) {
                return FluidStack.EMPTY;
            }

            tank = (FluidTankAddon)tanks.next();
        } while(tank.drain(resource, FluidAction.SIMULATE).isEmpty());

        return tank.drain(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        Iterator tanks = this.tanks.iterator();

        FluidTankAddon tank;
        do {
            if (!tanks.hasNext()) {
                return FluidStack.EMPTY;
            }

            tank = (FluidTankAddon)tanks.next();
        } while(tank.drain(maxDrain, FluidAction.SIMULATE).isEmpty());

        return tank.drain(maxDrain, action);
    }
}
