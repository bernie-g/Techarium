package software.bernie.techarium.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import software.bernie.techarium.pipe.networks.Filter;
import software.bernie.techarium.registry.ItemGroupRegistry;

public class FilterItem<ToTransport> extends Item {
    public FilterItem() {
        this(new Properties().tab(ItemGroupRegistry.TECHARIUM));
    }
    public FilterItem(Properties properties) {
        super(properties);
    }

    public Filter<ToTransport> getFilter(ItemStack itemStack) {
        return new Filter<ToTransport>() {};
    }
}
