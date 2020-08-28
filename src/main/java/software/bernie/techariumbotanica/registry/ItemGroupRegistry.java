package software.bernie.techariumbotanica.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroupRegistry
{
	public static final ItemGroup TECHARIUMS = new ItemGroup("techariums") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Item.getItemFromBlock(BlockRegistry.BOTARIUM_TIER_1.get()));
		}
	};
}
