package software.bernie.techarium.recipe.recipe.assembler;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import software.bernie.techarium.util.Utils;

public class AssemblerRecipePatern {

	public static final int craftingGridSize = 9;
	public static final String S_EMPTY = "O";
	public static final String S_ITEM = "X";
	
	private NonNullList<Ingredient> recipePatern;
	
	public AssemblerRecipePatern() {
		this.recipePatern = NonNullList.withSize(craftingGridSize, Ingredient.EMPTY);
	}

	public AssemblerRecipePatern setItemInSlot(Ingredient stack, int ... indexs) {
		for (int index : indexs) {
			recipePatern.set(index, stack);
		}
		return this;
	}
	
	public Ingredient getItem(int index) {
		return recipePatern.get(index);
	}
	
	public boolean isRecipeValide(List<ItemStack> inventory, boolean isShapeless) {	
		if (isShapeless) return AssemblerRecipeHelper.checkShapeless(inventory, recipePatern);
		else return AssemblerRecipeHelper.checkNonShapeless(inventory, recipePatern, 3, 3);
	}
	
	public static void saveJson(JsonObject json, AssemblerRecipePatern patern) {
		String stringPatern = "";
		
		for (Ingredient ingredient : patern.recipePatern)
			stringPatern += ingredient.isEmpty() ? S_EMPTY : S_ITEM;
		
		json.addProperty("patern", stringPatern);
				
		for (int i = 0; i < craftingGridSize; i++) {
			if (Character.toString(stringPatern.charAt(i)).equals(S_ITEM))
				json.add("slot_" + i, patern.recipePatern.get(i).toJson());
		}
	}
	
	public static void loadJson(JsonObject json, AssemblerRecipePatern patern) {
		String stringPatern = json.get("patern").getAsString();
		
		for (int i = 0; i < craftingGridSize; i++) {
			if (Character.toString(stringPatern.charAt(i)).equals(S_ITEM))
				patern.setItemInSlot(Utils.deserializeIngredient(json, "slot_" + i), i);
		}
	}
		
}
