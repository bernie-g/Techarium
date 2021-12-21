package software.bernie.techarium.recipe.recipe.assembler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import software.bernie.techarium.helper.ItemsHelper;

public class AssemblerRecipeHelper {

	private static int GRID_SIDE = 3; 
	private static int GRID_SIZE = GRID_SIDE * GRID_SIDE; 
	
	public static boolean checkShapeless(List<ItemStack> inputs, List<Ingredient> recipe) {		
		List<Ingredient> ingredients = new ArrayList<>(recipe);
		ingredients.removeIf(Ingredient -> Ingredient.isEmpty());
		
		List<ItemStack> inputCopy = new ArrayList<>(inputs);
		
		for (int i = 0; i < GRID_SIZE; i++) {
			removeDuplicate(inputCopy, ingredients);
		}
		
		for (ItemStack stack : inputCopy) {
			if (!stack.isEmpty()) return false;
		}		
		return ingredients.size() == 0;
	}

	public static void removeDuplicate(List<ItemStack> inputs, List<Ingredient> recipe) {
		if (recipe.isEmpty() || inputs.isEmpty())
			return;
		
		for (int i = 0; i < inputs.size(); i++) {
			ItemStack stack = inputs.get(i);
			
			
			if (stack.isEmpty()) {
				inputs.remove(stack);
				return;
			}
				
			for (Ingredient ingredient : recipe) {				
				if (ingredient.test(stack)) {
					recipe.remove(ingredient);
					inputs.remove(stack);
					return;
				}
			}
		}
	}	

	public static boolean checkNonShapeless(List<ItemStack> inputs, List<Ingredient> recipe, int width, int height) {
		
		List<Ingredient> ingredients = new ArrayList<>(recipe);
		List<ItemStack> inputCopy = new ArrayList<>(inputs);
		
		int offsetX = GRID_SIDE - width;
		int offsetY = GRID_SIDE - height;
						
		for (int i = 0; i < offsetX + 1; i++) {
			for (int j = 0; j < offsetY + 1; j++) {
				if (testRecipeWithOffset(inputCopy, ingredients, i, j, width, height)) return true;
			}
		}		
		return false;
	}
	
	private static boolean testRecipeWithOffset(List<ItemStack> inputs, List<Ingredient> recipe, int offsetX, int offsetY, int sx, int sy) {		
		for (int x = 0; x < GRID_SIDE; x++) {
			for (int y = 0; y < GRID_SIDE; y++) {
				
				int positionInputs = convert2dto1d(x, y);
				
				if (x >= offsetX && 
					x < sx + offsetX &&
					y >= offsetY && 
					y < sy + offsetY) {
					
					int px1 = x - offsetX;
					int py1 = y - offsetY;
					
					if (!recipe.get((py1 * sx) + px1).test(inputs.get(positionInputs)))
						return false;
				} else {
					if (!inputs.get(positionInputs).isEmpty())
						return false;
				}
			}
		}		
		return true;
	}
	
	private static int convert2dto1d(int posX, int posY) {
		return posX + (posY * GRID_SIDE);
	}	
}
