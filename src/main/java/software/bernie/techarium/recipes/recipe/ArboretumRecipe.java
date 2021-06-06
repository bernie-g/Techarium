package software.bernie.techarium.recipes.recipe;

import com.google.gson.Gson;
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
import software.bernie.techarium.util.JsonCodecUtils;

import static software.bernie.techarium.registry.RecipeRegistry.*;

public class ArboretumRecipe extends AbstractMachineRecipe {

    @Getter
    private final Ingredient cropType;
    @Getter
    private final FluidStack fluidIn;
    @Getter
    private final Ingredient soilIn;
    private final Ingredient output;

    @Builder(buildMethodName = "construct")
    public ArboretumRecipe(ResourceLocation id, Ingredient cropType, FluidStack fluidIn, Ingredient soilIn, Ingredient output, int progressPerTick, int maxProgress, int rfPerTick) {
        super(id, ARBORETUM_RECIPE_TYPE, progressPerTick, maxProgress, rfPerTick);
        this.cropType = cropType;
        this.fluidIn = fluidIn;
        this.soilIn = soilIn;
        this.output = output;
    }

    @Override
    public ItemStack getResultItem() {
        return super.getResultItem();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ARBORETUM_SERIALIZER.get();
    }

    public Ingredient getOutput() {
        return output;
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
            json.add("output", getOutput().toJson());
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
