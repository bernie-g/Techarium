package software.bernie.techarium.recipe.recipe.assembler;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import software.bernie.techarium.util.Utils;

public class AssemblerRecipePatern {

	public static final int craftingGridSize = 9;
	public static final String S_EMPTY = "O";
	public static final String S_ITEM = "X";
	
	private Ingredient [] recipePatern;
	
	public AssemblerRecipePatern() {
		this.recipePatern = new Ingredient [craftingGridSize];
		initPatern();
	}
	
	private void initPatern() {
		for (int i = 0; i < craftingGridSize; i ++) {
			recipePatern[i] = Ingredient.EMPTY;
		}
	}

	public AssemblerRecipePatern setItemInSlot(int index, Ingredient stack) {
		recipePatern[index] = stack;
		return this;
	}
	
	public Ingredient getItem(int index) {
		return recipePatern[index];
	}
	
	public boolean isRecipeValide(NonNullList<ItemStack> inventory, boolean isShapeless) {		
		if (isShapeless) return AssemblerRecipeHelper.checkShapeless(recipePatern, inventory);
		else return AssemblerRecipeHelper.checkNonShapeless(recipePatern, inventory);
	}
	
	public static void saveJson(JsonObject json, AssemblerRecipePatern patern) {
		String stringPatern = "";
		for (int i = 0; i < craftingGridSize; i++)
			stringPatern += patern.recipePatern[i].isEmpty() ? S_EMPTY : S_ITEM;
		
		json.addProperty("patern", stringPatern);
		
		for (int i = 0; i < craftingGridSize; i++) {
			if (Character.toString(stringPatern.charAt(i)).equals(S_ITEM))
				json.add("slot_" + i, patern.recipePatern[i].toJson());
		}
	}
	
	public static void loadJson(JsonObject json, AssemblerRecipePatern patern) {
		String stringPatern = json.get("patern").getAsString();
		
		for (int i = 0; i < craftingGridSize; i++) {
			if (Character.toString(stringPatern.charAt(i)).equals(S_ITEM))
				patern.setItemInSlot(i, Utils.deserializeIngredient(json, "slot_" + i));
		}
	}
		
}
