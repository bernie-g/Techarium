package software.bernie.techarium.pipes.networks;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;

@Getter
@Setter
public class FluidFilter implements Filter<FluidStack> {
    Fluid fluid;

    @Override
    public boolean canPassThrough(FluidStack fluidStack) {
        return (fluid == null || fluid == fluidStack.getFluid());
    }
}
