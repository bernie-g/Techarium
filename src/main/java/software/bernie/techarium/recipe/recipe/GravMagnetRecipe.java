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
import software.bernie.techarium.util.JsonCodecUtils;

public class GravMagnetRecipe extends AbstractMachineRecipe {
	@Getter
	@Setter
	ItemStack output1;
	
	@Getter
	@Setter
	ItemStack output2;
	
	@Getter
	@Setter
	ItemStack input;
	
	@Getter
	@Setter
	int processTime;
	
	@Getter
	@Setter
	boolean pull;
	
	@Builder(buildMethodName = "construct")
	public GravMagnetRecipe(ResourceLocation id, ItemStack output1, ItemStack output2, ItemStack input, int processTime, boolean pull) {
		super(id, RecipeRegistry.GRAVMAGNET_RECIPE_TYPE, 0, 0, 0);
		
		this.output1 	 = output1;
		this.output2 	 = output2;
		this.input   	 = input;
		this.processTime = processTime;
		this.pull	 	 = pull;
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
        	json.add("output1", JsonCodecUtils.serialize(getOutput1()));
        	json.add("output2", JsonCodecUtils.serialize(getOutput2()));
        	json.add("input",   JsonCodecUtils.serialize(getInput()));
        	json.addProperty("pull", pull);
        	json.addProperty("processTime", processTime);
        }
    }
}
