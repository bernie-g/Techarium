package software.bernie.techarium.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.api.CropType;

import static software.bernie.techarium.recipes.TechariumRecipeTypes.BOTARIUM_RECIPE_TYPE;

public class BotariumRecipe extends AbstractMachineRecipe {

    private final CropType type;
    private final FluidStack fluidIn;
    private final Ingredient soilIn;


    protected BotariumRecipe(ResourceLocation id, CropType type, FluidStack fluidIn, Ingredient soilIn, int maxProgress, int energyCost) {
        super(id, BOTARIUM_RECIPE_TYPE, maxProgress, energyCost);
        this.type = type;
        this.fluidIn = fluidIn;
        this.soilIn = soilIn;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }
}
