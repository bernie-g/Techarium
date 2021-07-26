package software.bernie.techarium.recipe.recipe.assembler;

import java.util.ArrayList;
import java.util.List;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import software.bernie.techarium.helper.ItemsHelper;

public class AssemblerRecipeHelper {

	public static boolean checkShapeless(Ingredient [] recipePatern, NonNullList<ItemStack> inventory) {
		if (AssemblerRecipeHelper.isInventoryEmpty(inventory)) return false;
		
		List<Ingredient> alreadyCount = new ArrayList<Ingredient>();
		
		 // Empty ingredient share same reference, can't be count using the methode below
		 // So im using this little trick to check the amount of empty space, if not the same, the recipie is not the same too
		if (!sameAmountOfEmptySpace(recipePatern, inventory)) return false;
		
		for (int i = 0; i < AssemblerRecipePatern.craftingGridSize; i++) {
			ItemStack stack = inventory.get(i);
			
			RESULT result = isInside(stack, recipePatern, alreadyCount);
			if (result == RESULT.NOT_HERE) return false;
		}
		
		return true;
	}
	
	private static boolean sameAmountOfEmptySpace(Ingredient [] recipePatern, NonNullList<ItemStack> inventory) {
		int amountPatern = 0;
		int amountInventory = 0;
		
		for (Ingredient i : recipePatern) {
			if (i.isEmpty()) amountPatern++;
		}
		
		for (int i = 0; i < AssemblerRecipePatern.craftingGridSize; i++) {
			if (inventory.get(i).isEmpty()) amountInventory++;
		}
		
		return amountPatern == amountInventory;
	}

	private static RESULT isInside(ItemStack stack, Ingredient [] recipePatern, List<Ingredient> alreadyCount) {
		if (stack.isEmpty()) return RESULT.COUNT; 
		
		for (Ingredient ingredient : recipePatern) {
			if (isAlreadyCount(ingredient, alreadyCount)) continue;
			
			if (ItemsHelper.isItemInIngredient(stack, ingredient)) {
				alreadyCount.add(ingredient);
				return RESULT.COUNT;
			}
		}
		
		return RESULT.NOT_HERE;
	}

	private static boolean isAlreadyCount(Ingredient check, List<Ingredient> alreadyCount) {
		for (Ingredient ingredient : alreadyCount) {			
			if (ingredient.equals(check)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isInventoryEmpty(NonNullList<ItemStack> inventory) {
		for (int i = 0; i < AssemblerRecipePatern.craftingGridSize; i++) {
			if (!inventory.get(i).isEmpty()) return false;
		}
		return true;
	}
	
	public static boolean checkNonShapeless(Ingredient [] recipePatern, NonNullList<ItemStack> inventory) {
		if (AssemblerRecipeHelper.isInventoryEmpty(inventory)) return false;
		
		int offsetX = getOffsetRight(recipePatern);
		int offsetY = getOffsetDown(recipePatern);
		
		if (offsetX == 0 && offsetY == 0)
			return checkBasicPatern(recipePatern, inventory);
		else {
			return checkComplexPatern(recipePatern, inventory, offsetX, offsetY);
		}
	}
		
	private static boolean checkBasicPatern(Ingredient [] recipePatern, NonNullList<ItemStack> inventory) {
		for (int i = 0; i < AssemblerRecipePatern.craftingGridSize; i++) {
			if (recipePatern[i].isEmpty() && inventory.get(i).isEmpty()) continue;
			
			if (!ItemsHelper.isItemInIngredient(inventory.get(i), recipePatern[i])) {
				return false;
			}
		}
		return true;
	}
	
	private static int getLength(Ingredient [] recipePatern) {
		int lastItem = 0;
		
		for (int i = 0; i < recipePatern.length; i++) {
			if (!recipePatern[i].isEmpty()) lastItem = i + 1;
		}
		
		return lastItem;
	}
	
	private static boolean checkComplexPatern(Ingredient [] recipePatern, NonNullList<ItemStack> inventory, int offsetX, int offsetY) {
		int length = getLength(recipePatern);
				
		for (int i = 0; i <= offsetX; i++) {
			for (int j = 0; j <= offsetY; j++) {
				if (checkPaternOffset(recipePatern, inventory, i, j, length)) return true;
			}
		}
		
		return false;
	}
	
	private static boolean checkPaternOffset(Ingredient [] recipePatern, NonNullList<ItemStack> inventory, int offsetX, int offsetY, int length) {
		int arrayOffset = (offsetY * 3) + offsetX;
				
		for (int i = 0; i < length; i++) {
			if (!ItemsHelper.isItemInIngredient(inventory.get(i + arrayOffset), recipePatern[i])) return false;
		}
		
		for (int i = 0; i < arrayOffset; i++) {
			if (!inventory.get(i).isEmpty()) return false;
		}
		
		for (int i = length + arrayOffset; i < 9; i++) {
			if (!inventory.get(i).isEmpty()) return false;
		}
		
		return true;
	}
	
	private static boolean checkRow(Ingredient [] recipePatern, int number) {
		for (int i = 0; i < 3; i++) {
			int pos = (number * 3) + i;
			if (!recipePatern[pos].isEmpty()) return false;
		}
		return true;
	}
	
	private static boolean checkColumns(Ingredient [] recipePatern, int number) {
		for (int i = 0; i < 3; i++) {
			int pos = number + (i * 3);
			if (!recipePatern[pos].isEmpty()) return false;
		}
		return true;
	}
	
	private static int getOffsetRight(Ingredient [] recipePatern) {
		int offsetX = 0;
		
		for (int i = 2; i > 0; i--) {
			if (!checkColumns(recipePatern, i)) return offsetX;
			offsetX++;
		}
		
		return offsetX;
	}
	
	private static int getOffsetDown(Ingredient [] recipePatern) {
		int offsetY = 0;
		
		for (int i = 2; i > 0; i--) {
			if (!checkRow(recipePatern, i)) return offsetY;
			offsetY++;
		}
		
		return offsetY;
	}

	private static enum RESULT {
		COUNT,
		NOT_HERE;
	}
	
	
}
