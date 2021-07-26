package software.bernie.techarium.helper;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ItemsHelper {

	public static ItemStack getFirstIngredient(Ingredient ingredient) {
		if (ingredient == null) return ItemStack.EMPTY;
		
		ItemStack[] stacks = ingredient.getItems();
		if (stacks.length == 0) return ItemStack.EMPTY;
		return stacks[0];
	}
	
	public static boolean isItemInIngredient(ItemStack stack, Ingredient ingredient) {
		if (ingredient == null) return false;
		
		if (ingredient.isEmpty() && stack.isEmpty()) return true;
		
		ItemStack[] stacks = ingredient.getItems();
		if (stacks.length == 0) return false;
		
		for (ItemStack s : stacks) {
			if (ItemStack.isSame(s, stack))
				return true;
		}
	
		return false;
	}

	public static boolean areSameIngredient(Ingredient ingredient1, Ingredient ingredient2) {
		for (ItemStack itemStack : ingredient1.getItems()) {
			if (isItemInIngredient(itemStack, ingredient2)) return true;
		}
		return false;
	}
	
	public static boolean isItemInList(List<ItemStack> stacks, ItemStack stack) {
		for (ItemStack s : stacks) {
			if (ItemStack.isSame(s, stack)) return true;
		}
		return false;
	}

	public static boolean mergeItemStacks(ItemStack stack1, ItemStack stack2) {		
		if (!ItemStack.isSame(stack1, stack2) || stack2.isEmpty()) return false;
		
		int spaceLeft = stack1.getMaxStackSize() - stack1.getCount();
		
		if (spaceLeft < stack2.getCount()) return false;
		
		stack1.grow(stack2.getCount());
		return true;
		
	}
}
