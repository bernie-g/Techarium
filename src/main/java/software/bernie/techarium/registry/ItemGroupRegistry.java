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
		public ItemStack createIcon() {
			return new ItemStack(BOTARIUM.getItem());
		}

		@Override
		public void fill(NonNullList<ItemStack> items) {
			super.fill(items);
		}
	};
}
