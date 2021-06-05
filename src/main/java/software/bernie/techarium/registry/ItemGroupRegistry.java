package software.bernie.techarium.registry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import software.bernie.techarium.item.PipeItem;

import static software.bernie.techarium.registry.BlockTileRegistry.BOTARIUM;

public class ItemGroupRegistry
{
	public static final ItemGroup TECHARIUMS = new ItemGroup("techarium") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(BOTARIUM.getItem());
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> items) {
			super.fillItemList(items);
		}
	};
}
