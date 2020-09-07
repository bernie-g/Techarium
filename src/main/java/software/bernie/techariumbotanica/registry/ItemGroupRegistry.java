package software.bernie.techariumbotanica.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import static software.bernie.techariumbotanica.registry.BlockTileRegistry.BOTANIUM;

public class ItemGroupRegistry
{
	public static final ItemGroup TECHARIUMS = new ItemGroup("techariums") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Item.getItemFromBlock(BOTANIUM.get()));
		}
	};
}
