package software.bernie.techarium.recipe.serializer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;
import software.bernie.techarium.util.ChancedItemStackList;
import software.bernie.techarium.util.JsonCodecUtils;

public class GravMagnetRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
		implements IRecipeSerializer<GravMagnetRecipe> {

	@Override
	public GravMagnetRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		ItemStack input = JsonCodecUtils.deserializeItemStack(json.get("input"));
		ChancedItemStackList output = ChancedItemStackList.fromJSON(json.get("output").getAsJsonArray());
		int time = json.get("processTime").getAsInt();
		boolean pull = json.get("pull").getAsBoolean();

		return new GravMagnetRecipe(recipeId, output, input, time, pull);
	}

	@Nullable
	@Override
	public GravMagnetRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
		ItemStack input = buffer.readItem();
		ChancedItemStackList output = ChancedItemStackList.read(buffer);
		int time = buffer.readInt();
		boolean pull = buffer.readBoolean();
		return new GravMagnetRecipe(recipeId, output, input, time, pull);
	}

	@Override
	public void toNetwork(PacketBuffer buffer, GravMagnetRecipe recipe) {
		buffer.writeItem(recipe.getInput());
		recipe.getOutput().write(buffer);
		buffer.writeInt(recipe.getProcessTime());
		buffer.writeBoolean(recipe.isPull());

	}
}
