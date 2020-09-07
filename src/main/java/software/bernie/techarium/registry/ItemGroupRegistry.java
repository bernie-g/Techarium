package software.bernie.techarium.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import static software.bernie.techarium.registry.BlockTileRegistry.BOTANIUM;

public class ItemGroupRegistry
{
	public static final ItemGroup TECHARIUMS = new ItemGroup("techarium") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(BOTANIUM.getItem());
		}
	};
}
