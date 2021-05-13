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
    public BotariumRecipe read(ResourceLocation recipeId, JsonObject json) {
        Ingredient crop = Utils.deserializeIngredient(json, "cropIn");
        FluidStack fluidIn = JsonCodecUtils.deserializeFluidStack(json.get("fluidIn"));
        Ingredient soil = Utils.deserializeIngredient(json, "soilIn");
        ItemStack output = JsonCodecUtils.deserializeItemStack(json.get("output"));
        int maxProgress = JSONUtils.getInt(json, "maxProgress");
        int ticksPerProgress = JSONUtils.getInt(json, "progressPerTick");
        int energy = JSONUtils.getInt(json, "rfPerTick");
        return new BotariumRecipe(recipeId, crop, fluidIn, soil, output, ticksPerProgress, maxProgress, energy);
    }

    @Nullable
    @Override
    public BotariumRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient crop = Ingredient.read(buffer);
        FluidStack fluidIn = buffer.readFluidStack();
        Ingredient soil = Ingredient.read(buffer);
        ItemStack output = buffer.readItemStack();
        int maxProgress = buffer.readInt();
        int ticksPerProgress = buffer.readInt();
        int energy = buffer.readInt();
        return new BotariumRecipe(recipeId, crop, fluidIn, soil, output, ticksPerProgress, maxProgress, energy);
    }

    @Override
    public void write(PacketBuffer buffer, BotariumRecipe recipe) {
        recipe.getCropType().write(buffer);
        buffer.writeFluidStack(recipe.getFluidIn());
        recipe.getSoilIn().write(buffer);
        buffer.writeItemStack(recipe.getRecipeOutput());
        buffer.writeInt(recipe.getMaxProgress());
        buffer.writeInt(recipe.getProgressPerTick());
        buffer.writeInt(recipe.getRfPerTick());
    }
}
