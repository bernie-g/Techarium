package software.bernie.techarium.recipes.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.api.CropType;
import software.bernie.techarium.recipes.AbstractMachineRecipe;

import static software.bernie.techarium.registry.RecipeSerializerRegistry.BOTARIUM_RECIPE_TYPE;
import static software.bernie.techarium.registry.RecipeSerializerRegistry.BOTARIUM_SERIALIZER;

public class ExchangeStationRecipe extends AbstractMachineRecipe {

    private final CropType cropType;
    private final FluidStack fluidIn;
    private final Ingredient soilIn;

    public ExchangeStationRecipe(ResourceLocation id, CropType cropType, FluidStack fluidIn, Ingredient soilIn, int tickRate, int maxProgress, int energyCost) {
        super(id, BOTARIUM_RECIPE_TYPE, tickRate, maxProgress, energyCost);
        this.cropType = cropType;
        this.fluidIn = fluidIn;
        this.soilIn = soilIn;
    }


    public CropType getCropType(){
        return cropType;
    }

    public FluidStack getFluidIn() {
        return fluidIn;
    }

    public Ingredient getSoilIn() {
        return soilIn;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BOTARIUM_SERIALIZER.get();
    }


}
