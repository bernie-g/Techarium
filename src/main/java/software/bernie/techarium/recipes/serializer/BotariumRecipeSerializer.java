package software.bernie.techarium.recipes.serializer;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.util.JsonCodecUtils;
import software.bernie.techarium.util.Utils;

import javax.annotation.Nullable;

public class BotariumRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BotariumRecipe> {

    @Override
    public BotariumRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient crop = Utils.deserializeIngredient(json, "cropIn");
        FluidStack fluidIn = JsonCodecUtils.deserializeFluidStack(json.get("fluidIn"));
        Ingredient soil = Utils.deserializeIngredient(json, "soilIn");
        ItemStack output = JsonCodecUtils.deserializeItemStack(json.get("output"));
        int maxProgress = JSONUtils.getAsInt(json, "maxProgress");
        int ticksPerProgress = JSONUtils.getAsInt(json, "progressPerTick");
        int energy = JSONUtils.getAsInt(json, "rfPerTick");
        return new BotariumRecipe(recipeId, crop, fluidIn, soil, output, ticksPerProgress, maxProgress, energy);
    }

    @Nullable
    @Override
    public BotariumRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient crop = Ingredient.fromNetwork(buffer);
        FluidStack fluidIn = buffer.readFluidStack();
        Ingredient soil = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int maxProgress = buffer.readInt();
        int ticksPerProgress = buffer.readInt();
        int energy = buffer.readInt();
        return new BotariumRecipe(recipeId, crop, fluidIn, soil, output, ticksPerProgress, maxProgress, energy);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, BotariumRecipe recipe) {
        recipe.getCropType().toNetwork(buffer);
        buffer.writeFluidStack(recipe.getFluidIn());
        recipe.getSoilIn().toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem());
        buffer.writeInt(recipe.getMaxProgress());
        buffer.writeInt(recipe.getProgressPerTick());
        buffer.writeInt(recipe.getRfPerTick());
    }
}
