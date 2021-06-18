package software.bernie.techarium.recipes.serializer;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.recipes.recipe.ExchangeStationRecipe;
import software.bernie.techarium.util.JsonCodecUtils;
import software.bernie.techarium.util.Utils;

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
