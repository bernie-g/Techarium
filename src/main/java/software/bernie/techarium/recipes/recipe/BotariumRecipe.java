package software.bernie.techarium.recipes.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.api.CropType;
import software.bernie.techarium.recipes.AbstractMachineRecipe;

import java.util.List;

import static software.bernie.techarium.registry.RecipeSerializerRegistry.BOTARIUM_RECIPE_TYPE;
import static software.bernie.techarium.registry.RecipeSerializerRegistry.BOTARIUM_SERIALIZER;

public class BotariumRecipe extends AbstractMachineRecipe {

    private final CropType type;
    private final FluidStack fluidIn;
    private final Ingredient soilIn;


    public BotariumRecipe(ResourceLocation id, CropType type, FluidStack fluidIn, Ingredient soilIn, int maxProgress, int energyCost) {
        super(id, BOTARIUM_RECIPE_TYPE, maxProgress, energyCost);
        this.type = type;
        this.fluidIn = fluidIn;
        this.soilIn = soilIn;
    }

    public CropType getCropType(){
        return type;
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
