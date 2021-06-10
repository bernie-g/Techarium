package software.bernie.techarium.item;

import lombok.Getter;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import software.bernie.techarium.pipe.networks.Filter;
import software.bernie.techarium.pipe.util.PipeType;
import software.bernie.techarium.registry.BlockTileRegistry;
import software.bernie.techarium.registry.ItemGroupRegistry;

@Getter
public class PipeItem extends BlockItem {

    private final PipeType type;

    public PipeItem(PipeType type) {
        super(BlockTileRegistry.PIPE.getBlock(), new Properties().tab(ItemGroupRegistry.TECHARIUMS));
        this.type = type;
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            items.add(new ItemStack(this));
        }
    }

    @Override
    public String getDescriptionId() {
        return getOrCreateDescriptionId();
    }

}
