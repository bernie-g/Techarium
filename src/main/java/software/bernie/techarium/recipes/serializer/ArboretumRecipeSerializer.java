package software.bernie.techarium.recipes.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.techarium.recipes.recipe.ArboretumRecipe;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.util.JsonCodecUtils;
import software.bernie.techarium.util.Utils;

import javax.annotation.Nullable;

public class ArboretumRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ArboretumRecipe> {

    @Override
    public ArboretumRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient crop = Utils.deserializeIngredient(json, "cropIn");
        FluidStack fluidIn = JsonCodecUtils.deserializeFluidStack(json.get("fluidIn"));
        Ingredient soil = Utils.deserializeIngredient(json, "soilIn");
        Ingredient output = Utils.deserializeIngredient(json, "output");
        int maxProgress = JSONUtils.getAsInt(json, "maxProgress");
        int ticksPerProgress = JSONUtils.getAsInt(json, "progressPerTick");
        int energy = JSONUtils.getAsInt(json, "rfPerTick");
        return new ArboretumRecipe(recipeId, crop, fluidIn, soil, output, ticksPerProgress, maxProgress, energy);
    }

    @Nullable
    @Override
    public ArboretumRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient crop = Ingredient.fromNetwork(buffer);
        FluidStack fluidIn = buffer.readFluidStack();
        Ingredient soil = Ingredient.fromNetwork(buffer);
        Ingredient output = Ingredient.fromNetwork(buffer);
        int maxProgress = buffer.readInt();
        int ticksPerProgress = buffer.readInt();
        int energy = buffer.readInt();
        return new ArboretumRecipe(recipeId, crop, fluidIn, soil, output, ticksPerProgress, maxProgress, energy);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, ArboretumRecipe recipe) {
        recipe.getCropType().toNetwork(buffer);
        buffer.writeFluidStack(recipe.getFluidIn());
        recipe.getSoilIn().toNetwork(buffer);
        recipe.getOutput().toNetwork(buffer);
        buffer.writeInt(recipe.getMaxProgress());
        buffer.writeInt(recipe.getProgressPerTick());
        buffer.writeInt(recipe.getRfPerTick());
    }
}
