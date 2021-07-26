package software.bernie.techarium.recipe.recipe.assembler;

import com.google.gson.JsonObject;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.recipe.AbstractMachineRecipe;
import software.bernie.techarium.recipe.recipe.TechariumRecipeBuilder;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.util.JsonCodecUtils;

public class AssemblerRecipe extends AbstractMachineRecipe {

	@Getter
	@Setter
	private AssemblerRecipePatern recipePatern;
	
	@Getter
	@Setter
	private ItemStack output;
	
	@Getter
	@Setter
	private boolean isShapeless;
	
	@Builder(buildMethodName = "construct")
	public AssemblerRecipe(ResourceLocation id, AssemblerRecipePatern recipePatern, ItemStack output, boolean isShapeless) {
		super(id, RecipeRegistry.ASSEMBLER_RECIPE_TYPE, 0, 0, 0);
		
		this.recipePatern = recipePatern;
		this.output = output;
		this.isShapeless = isShapeless;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RecipeRegistry.ASSEMBLER_SERIALIZER.get();
	}

	@Override
	protected TechariumRecipeBuilder.Result getResult(ResourceLocation id) {
		return new Result(id);
	}
	
	public class Result extends AbstractMachineRecipe.Result {

		public Result(ResourceLocation id) {
			super(id);
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty("isShapeless", isShapeless);
			json.add("output", JsonCodecUtils.serialize(getOutput()));
			AssemblerRecipePatern.saveJson(json, recipePatern);
		}
	}

}
