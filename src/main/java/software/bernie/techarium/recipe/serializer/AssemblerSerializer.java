package software.bernie.techarium.recipe.serializer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.techarium.recipe.recipe.assembler.AssemblerRecipe;
import software.bernie.techarium.recipe.recipe.assembler.AssemblerRecipePatern;
import software.bernie.techarium.util.JsonCodecUtils;

public class AssemblerSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AssemblerRecipe> {

	@Override
	public AssemblerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		
		boolean isShapeless = json.get("isShapeless").getAsBoolean();
		ItemStack output = JsonCodecUtils.deserializeItemStack(json.get("output"));
		
		AssemblerRecipePatern patern = new AssemblerRecipePatern();
		AssemblerRecipePatern.loadJson(json, patern);

		return new AssemblerRecipe(recipeId, patern, output, isShapeless);
	}

	@Nullable
	@Override
	public AssemblerRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
		boolean isShapeless = buffer.readBoolean(); 
		ItemStack output = buffer.readItem();
		
		AssemblerRecipePatern patern = new AssemblerRecipePatern();
		
		for (int i = 0; i < AssemblerRecipePatern.craftingGridSize; i++) {
			patern.setItemInSlot(i, Ingredient.fromNetwork(buffer));
		}
		
		return new AssemblerRecipe(recipeId, patern, output, isShapeless);
	}

	@Override
	public void toNetwork(PacketBuffer buffer, AssemblerRecipe recipe) {
		buffer.writeBoolean(recipe.isShapeless());
		buffer.writeItem(recipe.getOutput());
		
		for (int i = 0; i < AssemblerRecipePatern.craftingGridSize; i++) {
			recipe.getRecipePatern().getItem(i).toNetwork(buffer);
		}
	}
}
