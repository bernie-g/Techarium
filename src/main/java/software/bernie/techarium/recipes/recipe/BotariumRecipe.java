package software.bernie.techarium.recipes.recipe;

import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.recipes.AbstractMachineRecipe;
import software.bernie.techarium.util.ChancedItemStackList;
import software.bernie.techarium.util.JsonCodecUtils;

import static software.bernie.techarium.registry.RecipeRegistry.BOTARIUM_RECIPE_TYPE;
import static software.bernie.techarium.registry.RecipeRegistry.BOTARIUM_SERIALIZER;

public class BotariumRecipe extends AbstractMachineRecipe {

    @Getter
    private final Ingredient cropType;
    @Getter
    private final FluidStack fluidIn;
    @Getter
    private final Ingredient soilIn;
    @Getter
    private final ChancedItemStackList output;

    @Builder(buildMethodName = "construct")
    public BotariumRecipe(ResourceLocation id, Ingredient cropType, FluidStack fluidIn, Ingredient soilIn, ChancedItemStackList output, int progressPerTick, int maxProgress, int rfPerTick) {
        super(id, BOTARIUM_RECIPE_TYPE, progressPerTick, maxProgress, rfPerTick);
        this.cropType = cropType;
        this.fluidIn = fluidIn;
        this.soilIn = soilIn;
        this.output = output;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BOTARIUM_SERIALIZER.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
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
        public void serializeRecipeData(JsonObject json) {
            super.serializeRecipeData(json);
            json.add("cropIn", getCropType().toJson());
            json.add("soilIn", getSoilIn().toJson());
            json.add("fluidIn", JsonCodecUtils.serialize(getFluidIn()));
            json.add("output", getOutput().toJSON());
        }
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = super.getIngredients();
        if (soilIn != null) ingredients.add(soilIn);
        if (cropType != null) ingredients.add(cropType);
        if (cropType != null) ingredients.add(cropType);
        return ingredients;
    }
}
