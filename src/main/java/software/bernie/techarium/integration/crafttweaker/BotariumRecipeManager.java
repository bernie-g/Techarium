package software.bernie.techarium.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.openzen.zencode.java.ZenCodeType;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.RecipeRegistry;

@ZenRegister
@ZenCodeType.Name("mods.techarium.Botarium")
public class BotariumRecipeManager implements IRecipeManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IIngredient cropIn, IIngredient soilIn, IFluidStack inputFluid, IItemStack output, int progressPerTick, int maxProgress, int rfPerTick) {
        name = fixRecipeName(name);
        BotariumRecipe recipe = createBotariumRecipe(cropIn, name, inputFluid, soilIn, output, progressPerTick, maxProgress, rfPerTick);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }

    @NotNull
    private BotariumRecipe createBotariumRecipe(IIngredient cropIn, String name, IFluidStack inputFluid, IIngredient soilIn, IItemStack output, int progressPerTick, int maxProgress, int rfPerTick) {
        return new BotariumRecipe(new ResourceLocation("crafttweaker", name), cropIn.asVanillaIngredient(), inputFluid.getImmutableInternal(),
                soilIn.asVanillaIngredient(), output.getImmutableInternal(), progressPerTick, maxProgress, rfPerTick);
    }

    @Override
    public IRecipeType<BotariumRecipe> getRecipeType() {
        return RecipeRegistry.BOTARIUM_RECIPE_TYPE;
    }
}
