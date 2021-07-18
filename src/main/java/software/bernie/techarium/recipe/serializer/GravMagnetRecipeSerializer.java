package software.bernie.techarium.recipe.serializer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;
import software.bernie.techarium.util.JsonCodecUtils;

public class GravMagnetRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GravMagnetRecipe> {

    @Override
    public GravMagnetRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
    	ItemStack input   = JsonCodecUtils.deserializeItemStack(json.get("input"));
    	ItemStack output1 = JsonCodecUtils.deserializeItemStack(json.get("output1"));
    	ItemStack output2 = JsonCodecUtils.deserializeItemStack(json.get("output2"));
    	
    	int time = json.get("processTime").getAsInt();
    	boolean pull = json.get("pull").getAsBoolean();
    	
        return new GravMagnetRecipe(recipeId, output1, output2, input, time, pull);
    }

    @Nullable
    @Override
    public GravMagnetRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
    	ItemStack input   = buffer.readItem();
    	ItemStack output1 = buffer.readItem();
    	ItemStack output2 = buffer.readItem();
    	int time 		  = buffer.readInt();
    	boolean pull  	  = buffer.readBoolean();	
        return new GravMagnetRecipe(recipeId, output1, output2, input, time, pull);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, GravMagnetRecipe recipe) {
    	buffer.writeItem(recipe.getInput());
    	buffer.writeItem(recipe.getOutput1());
    	buffer.writeItem( recipe.getOutput2());
    	buffer.writeInt(recipe.getProcessTime());
    	buffer.writeBoolean(recipe.isPull());
    	
    }
}
