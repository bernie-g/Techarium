package software.bernie.techarium.recipes.serializer;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.util.Utils;

import javax.annotation.Nullable;

import static software.bernie.techarium.util.StaticHandler.deserializeFluid;

public class BotariumRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BotariumRecipe> {

    @Override
    public BotariumRecipe read(ResourceLocation recipeId, JsonObject json) {
        Ingredient crop = Utils.deserializeIngredient(json, "cropIn");
        FluidStack fluidIn = deserializeFluid(json);
        Ingredient soil = Utils.deserializeIngredient(json, "soilIn");
        ItemStack output = ItemStack.CODEC.parse(JsonOps.INSTANCE, json.get("output")).result().orElseThrow(
                () -> new IllegalStateException("Could not parse recipe output"));
        int maxProgress = JSONUtils.getInt(json, "maxProgress");
        int ticksPerProgress = JSONUtils.getInt(json, "ticksPerProgress");
        int energy = JSONUtils.getInt(json, "energyCost");
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
        buffer.writeInt(recipe.getTickRate());
        buffer.writeInt(recipe.getEnergyCost());
    }
}
