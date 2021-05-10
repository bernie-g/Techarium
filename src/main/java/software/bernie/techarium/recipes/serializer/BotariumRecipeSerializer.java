package software.bernie.techarium.recipes.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import software.bernie.techarium.api.CropType;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.TechariumCustomRegistries;

import javax.annotation.Nullable;

import java.util.Objects;

import static software.bernie.techarium.util.StaticHandler.deserializeFluid;

public class BotariumRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BotariumRecipe> {

    @Override
    public BotariumRecipe read(ResourceLocation recipeId, JsonObject json) {
        CropType type = TechariumCustomRegistries.CROP_TYPE.getValue(new ResourceLocation(JSONUtils.getString(json,"cropType")));
        FluidStack fluidIn = deserializeFluid(json);
        JsonElement jsonelement = (JSONUtils.isJsonArray(json, "soilIn") ? JSONUtils.getJsonArray(json, "soilIn") : JSONUtils.getJsonObject(json, "soilIn"));
        Ingredient ingredient = Ingredient.deserialize(jsonelement);
        int maxProgress = JSONUtils.getInt(json,"maxProgress");
        int ticksPerProgress = JSONUtils.getInt(json,"ticksPerProgress");
        int tier = JSONUtils.getInt(json,"machineTier");
        int energy = JSONUtils.getInt(json,"energyCost");
        return new BotariumRecipe(recipeId, type, fluidIn, ingredient, ticksPerProgress, maxProgress, energy);
    }

    @Nullable
    @Override
    public BotariumRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        CropType type = TechariumCustomRegistries.CROP_TYPE.getValue(new ResourceLocation(buffer.readString()));
        FluidStack fluidIn = buffer.readFluidStack();
        Ingredient ingredient = Ingredient.read(buffer);
        int maxProgress = buffer.readInt();
        int ticksPerProgress = buffer.readInt();
        int energy = buffer.readInt();
        return new BotariumRecipe(recipeId, type, fluidIn, ingredient, ticksPerProgress, maxProgress, energy);
    }

    @Override
    public void write(PacketBuffer buffer, BotariumRecipe recipe) {
        buffer.writeString(Objects.requireNonNull(recipe.getCropType().getRegistryName()).toString());
        buffer.writeFluidStack(recipe.getFluidIn());
        recipe.getSoilIn().write(buffer);
        buffer.writeInt(recipe.getMaxProgress());
        buffer.writeInt(recipe.getTickRate());
        buffer.writeInt(recipe.getEnergyCost());
    }
}
