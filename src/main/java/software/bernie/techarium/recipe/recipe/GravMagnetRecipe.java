package software.bernie.techarium.recipe.recipe;

import com.google.gson.JsonObject;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.recipe.AbstractMachineRecipe;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.util.ChancedItemStackList;
import software.bernie.techarium.util.JsonCodecUtils;

public class GravMagnetRecipe extends AbstractMachineRecipe {
	@Getter
	@Setter
	ChancedItemStackList output;

	@Getter
	@Setter
	ItemStack input;

	@Getter
	@Setter
	boolean pull;

	@Builder(buildMethodName = "construct")
	public GravMagnetRecipe(ResourceLocation id, ChancedItemStackList output, ItemStack input, int processTime,
			boolean pull) {
		super(id, RecipeRegistry.GRAVMAGNET_RECIPE_TYPE, 1, processTime, 0);

		this.output = output;
		this.input = input;
		this.pull = pull;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RecipeRegistry.GRAVMAGNET_SERIALIZER.get();
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
			super.serializeRecipeData(json);
			json.add("output", getOutput().toJSON());
			json.add("input", JsonCodecUtils.serialize(getInput()));
			json.addProperty("pull", pull);
		}
	}
}
