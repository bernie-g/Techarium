package software.bernie.techarium.pipes.networks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.capability.PipeType;

public class ItemPipeNetwork extends PipeNetwork<IItemHandler, ItemStack> {

    @Override
    public PipeType getType() {
        return PipeType.ITEM;
    }

    @Override
    public Filter<ItemStack> getFilter(PipePosition pipePosition) {
        return new ItemFilter();
    }

    @Override
    public boolean isEmpty(ItemStack toTransport) {
        return toTransport.isEmpty();
    }

    @Override
    public Capability<IItemHandler> getDefaultCapability() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public int getMaxRemove() {
        return 4;
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
    public ItemStack drainWith(IItemHandler capability, ItemStack drain, int slot, boolean simulate) {
        return capability.extractItem(slot, drain.getCount(), simulate);
    }


    @Override
    public ItemStack fill(IItemHandler capability, ItemStack toFill, boolean simulate) {
        ItemStack leftToInsert = toFill;
        for (int i = 0; i < getSlots(capability); i++) {
            leftToInsert = capability.insertItem(i, leftToInsert, simulate);
            if (leftToInsert.isEmpty())
                return toFill;
        }
        ItemStack wasInserted = toFill.copy();
        wasInserted.setCount(toFill.getCount()- leftToInsert.getCount());
        return wasInserted;
    }

    @Override
    public int getSlots(IItemHandler capability) {
        return capability.getSlots();
    }
}
