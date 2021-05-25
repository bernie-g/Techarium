package software.bernie.techarium.integration.mekanism;

import mekanism.common.registries.MekanismItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import software.bernie.techarium.integration.Integration;

/**
 * Anything in this class is safe to use without worrying about ClassNotfound's, the Wrapper takes care of the overhead
 */
public class MekanismIntegration extends Integration {
    public MekanismIntegration(String modID) {
        super(modID);
    }

    public boolean isGauge(Item item) {
        return item == MekanismItems.GAUGE_DROPPER.getItem();
    }

    public FluidStack drainGauge(int amount, ItemStack itemStack) {
        if (!isGauge(itemStack.getItem()))
            throw  new IllegalArgumentException("ItemStack is not a gaugeDropper");

        FluidStack stack = readFluidStackFromGauge(itemStack).copy();
        stack.setAmount(0);
        int alreadyRemoved = -1;
        while (alreadyRemoved != stack.getAmount() && stack.getAmount() != amount) {
            alreadyRemoved = stack.getAmount();
            FluidStack removed = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElseThrow(NullPointerException::new).drain(amount - alreadyRemoved, IFluidHandler.FluidAction.EXECUTE);
            stack.setAmount(stack.getAmount() + removed.getAmount());
        }
        return stack;
    }

    public int fillGauge(ItemStack itemStack, FluidStack toFill) {
        if (!isGauge(itemStack.getItem()))
            throw  new IllegalArgumentException("ItemStack is not a gaugeDropper");

        FluidStack stack = toFill.copy();
        stack.setAmount(0);
        int lastFilled = -1;
        while (lastFilled != stack.getAmount() && stack.getAmount() != toFill.getAmount()) {
            lastFilled = stack.getAmount();
            FluidStack fillWith = toFill.copy();
            fillWith.setAmount(toFill.getAmount() - stack.getAmount());
            int filled = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElseThrow(NullPointerException::new).fill(fillWith, IFluidHandler.FluidAction.EXECUTE);
            stack.setAmount(stack.getAmount() + filled);
        }
        return stack.getAmount();
    }

    public FluidStack readFluidStackFromGauge(ItemStack itemStack) {
        if (!isGauge(itemStack.getItem()))
            throw  new IllegalArgumentException("ItemStack is not a gaugeDropper");
        return itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElseThrow(NullPointerException::new).getFluidInTank(0).copy();
    }
}
