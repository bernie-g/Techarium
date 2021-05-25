package software.bernie.techarium.pipes.networks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import software.bernie.techarium.pipes.capability.PipeType;

public class ItemPipeNetwork extends PipeNetwork<IItemHandler, ItemStack> {
    @Override
    public boolean isType(PipeType type) {
        return type == PipeType.ITEM;
    }

    @Override
    public void tick() {

    }

    @Override
    public Capability<IItemHandler> getDefaultCapability() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public boolean canFill(IItemHandler capability, ItemStack itemStack) {
        return fill(capability, itemStack, true).equals(itemStack);
    }

    @Override
    public ItemStack drain(IItemHandler capability, int amount, int slot, boolean simulate) {
        return capability.extractItem(slot, amount, simulate);
    }


    @Override
    public ItemStack fill(IItemHandler capability, ItemStack itemStack, boolean simulate) {
        for (int i = 0; i < getSlots(capability); i++) {
            itemStack = capability.insertItem(i, itemStack, simulate);
            if (itemStack.isEmpty())
                return itemStack;
        }
        return itemStack;
    }

    @Override
    public int getSlots(IItemHandler capability) {
        return capability.getSlots();
    }
}
