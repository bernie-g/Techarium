package software.bernie.techarium.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.openzen.zencode.java.ZenCodeType;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.RecipeRegistry;

@ZenRegister
@ZenCodeType.Name("mods.techarium.Botarium")
public class BotariumRecipeManager implements IRecipeManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IIngredient cropIn, IIngredient soilIn, FluidStack inputFluid, ItemStack output, int tickRate, int maxProgress, int energyCost) {
        name = fixRecipeName(name);
        BotariumRecipe recipe = createBotariumRecipe(cropIn, name, inputFluid, soilIn, output, tickRate, maxProgress, energyCost);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }

    @NotNull
    private BotariumRecipe createBotariumRecipe(IIngredient cropIn, String name, FluidStack inputFluid, IIngredient soilIn, ItemStack output, int tickRate, int maxProgress, int energyCost) {
        return new BotariumRecipe(new ResourceLocation("crafttweaker", name), cropIn.asVanillaIngredient(), inputFluid,
                soilIn.asVanillaIngredient(), output, tickRate, maxProgress, energyCost);
    }

    @Override
    public IRecipeType getRecipeType() {
        return RecipeRegistry.BOTARIUM_RECIPE_TYPE;
    }
}
