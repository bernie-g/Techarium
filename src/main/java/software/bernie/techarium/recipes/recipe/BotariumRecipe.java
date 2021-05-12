package software.bernie.techarium.recipes.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.recipes.AbstractMachineRecipe;

import static software.bernie.techarium.registry.RecipeRegistry.BOTARIUM_RECIPE_TYPE;
import static software.bernie.techarium.registry.RecipeRegistry.BOTARIUM_SERIALIZER;

public class BotariumRecipe extends AbstractMachineRecipe {

    @Getter
    private final Ingredient cropType;
    @Getter
    private final FluidStack fluidIn;
    @Getter
    private final Ingredient soilIn;
    private final ItemStack output;

    @Builder(buildMethodName = "construct")
    public BotariumRecipe(ResourceLocation id, Ingredient cropType, FluidStack fluidIn, Ingredient soilIn, ItemStack output, int tickRate, int maxProgress, int energyCost) {
        super(id, BOTARIUM_RECIPE_TYPE, tickRate, maxProgress, energyCost);
        this.cropType = cropType;
        this.fluidIn = fluidIn;
        this.soilIn = soilIn;
        this.output = output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BOTARIUM_SERIALIZER.get();
    }

    @Override
    protected TechariumRecipeBuilder.Result getResult(ResourceLocation id) {
        return new Result(id);
    }

    public class Result extends AbstractMachineRecipe.Result {
        public Result(ResourceLocation id) {
            super(id);
        }

        @Override
        public void serialize(JsonObject json) {
            super.serialize(json);
            json.add("cropIn", getCropType().serialize());
            json.addProperty("fluidIn", getFluidIn().getFluid().getRegistryName().toString());
            json.addProperty("fluidAmount", getFluidIn().getAmount());
            json.add("soilIn", getSoilIn().serialize());
            json.add("output", ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, getRecipeOutput()).result().orElseThrow(
                    () -> new IllegalStateException("Could not encode output itemstack")));
        }
    }

}
