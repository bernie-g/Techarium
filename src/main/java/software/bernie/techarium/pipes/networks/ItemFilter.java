package software.bernie.techarium.pipes.networks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemFilter implements Filter<ItemStack> {
    private Item[] filtered = {Items.AIR,Items.AIR,Items.AIR,Items.AIR,Items.AIR};

    public void setFilter(int slot, Item item) {
        if (slot >= 5)
            return;
        filtered[slot] = item;
    }

    @Override
    public boolean canPassThrough(ItemStack itemStack) {
        for (Item item : filtered) {
            if (item == Items.AIR || item == itemStack.getItem())
                return true;
        }
        return false;
    }
}
