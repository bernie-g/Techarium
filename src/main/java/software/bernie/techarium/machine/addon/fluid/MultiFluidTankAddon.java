package software.bernie.techarium.machine.addon.fluid;

import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MultiFluidTankAddon implements IContainerComponentProvider {

    private final List<FluidTankAddon> fluidTanks = new ArrayList<>();
    private LazyOptional<MultiTankCapHandler> tankOptional;

    public MultiFluidTankAddon() {
        tankOptional = LazyOptional.empty();
    }

    public void add(@Nonnull FluidTankAddon addon) {
        this.fluidTanks.add(addon);
        this.rebuildCap();
    }

    public LazyOptional<MultiTankCapHandler> getTankOptional() {
        return tankOptional;
    }

    public List<FluidTankAddon> getFluidTanks() {
        return fluidTanks;
    }

    private void rebuildCap() {
        tankOptional = LazyOptional.of(() -> new MultiTankCapHandler(fluidTanks));
    }


    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        List<IFactory<? extends Slot>> components = new ArrayList<>();
        fluidTanks.forEach(posInv -> {
            components.addAll(posInv.getContainerComponents());
        });
        return components;
    }
}
