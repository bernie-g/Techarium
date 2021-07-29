package software.bernie.techarium.recipe.recipe;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.techarium.machine.interfaces.recipe.IMachineRecipe;
import software.bernie.techarium.recipe.AbstractMachineRecipe;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.util.JsonCodecUtils;
import software.bernie.techarium.util.LogCache;

@Getter
public class HammerRecipe extends AbstractMachineRecipe {
	private final Ingredient input0;
	private final Ingredient input1;
	private final Ingredient input2;
	private final ItemStack output;

	public HammerRecipe(ResourceLocation id, Ingredient input0, Ingredient input1, Ingredient input2, ItemStack output) {
		super(id, RecipeRegistry.HAMMER_RECIPE_TYPE, 1, Integer.MAX_VALUE, 0);
		this.input0 = input0;
		this.input1 = input1;
		this.input2 = input2;
		this.output = output;
	}

	public boolean validate() {
		try {
			validateIngredient(input0);
			validateIngredient(input1);
			validateIngredient(input2);
			validateItemStack(output);
			return true;
		} catch (IllegalStateException e) {
			LogCache.getLogger(HammerRecipe.class).warn(e.getMessage());
		}
		return false;
	}

	private static void validateIngredient(Ingredient ingredient) throws IllegalStateException{
		for (ItemStack itemStack : ingredient.getItems()) {
			validateItemStack(itemStack);
		}
	}
	private static void validateItemStack(ItemStack itemStack) throws IllegalStateException{
		if (itemStack.getCount() != 1)
			throw new IllegalArgumentException("itemStack count is not 1");
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RecipeRegistry.HAMMER_SERIALIZER.get();
	}

	@Override
	protected TechariumRecipeBuilder.Result getResult(ResourceLocation id) {
		return new Result(id);
	}
	public class Result extends TechariumRecipeBuilder.Result {

		public Result(ResourceLocation id) {
			super(id);
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("input0", input0.toJson());
			json.add("input1", input1.toJson());
			json.add("input2", input2.toJson());
			json.add("output", JsonCodecUtils.serialize(getOutput()));
		}
	}
}
