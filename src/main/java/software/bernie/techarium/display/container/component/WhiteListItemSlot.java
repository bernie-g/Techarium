package software.bernie.techarium.display.container.component;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import software.bernie.techarium.helper.ItemsHelper;

public class WhiteListItemSlot extends Slot{

	List<ItemStack> stacks;

	public WhiteListItemSlot(IInventory inv, int id, int posX, int posY, ItemStack ... itemStacks) {
		super(inv, id, posX, posY);
		stacks = new ArrayList<>();
		
		for (ItemStack stack : itemStacks) {
			stacks.add(stack);
		}
		
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		if (!ItemsHelper.isItemInList(stacks, stack)) return false;
		return super.mayPlace(stack);
	}
	
}
