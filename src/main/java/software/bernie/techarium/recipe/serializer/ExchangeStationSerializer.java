package software.bernie.techarium.recipe.serializer;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.recipe.recipe.ExchangeStationRecipe;
import software.bernie.techarium.util.JsonCodecUtils;

public class ExchangeStationSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ExchangeStationRecipe>
{
    @Override
    public ExchangeStationRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        ItemStack input = JsonCodecUtils.deserializeItemStack(json.get("itemIn"));
        ItemStack output = JsonCodecUtils.deserializeItemStack(json.get("itemOut"));
        return new ExchangeStationRecipe(recipeId, input, output);
    }

    @Nullable
    @Override
    public ExchangeStationRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        ItemStack input = buffer.readItem();
        ItemStack output = buffer.readItem();
        return new ExchangeStationRecipe(recipeId, input, output);
    }


    @Override
    public void toNetwork(PacketBuffer buffer, ExchangeStationRecipe recipe) {
        buffer.writeItem(recipe.getInput());
        buffer.writeItem(recipe.getOutput());
    }
}
