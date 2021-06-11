package software.bernie.techarium.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.item.MCWeightedItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.openzen.zencode.java.ZenCodeType;
import software.bernie.techarium.recipe.recipe.BotariumRecipe;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.util.ChancedItemStack;
import software.bernie.techarium.util.ChancedItemStackList;

import java.util.Arrays;

@ZenRegister
@ZenCodeType.Name("mods.techarium.Botarium")
public class BotariumRecipeManager implements IRecipeManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IIngredient cropIn, IIngredient soilIn, IFluidStack inputFluid, MCWeightedItemStack[] output, int progressPerTick, int maxProgress, int rfPerTick) {
        name = fixRecipeName(name);
        BotariumRecipe recipe = createBotariumRecipe(cropIn, name, inputFluid, soilIn, output, progressPerTick, maxProgress, rfPerTick);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }

    @NotNull
    private BotariumRecipe createBotariumRecipe(IIngredient cropIn, String name, IFluidStack inputFluid, IIngredient soilIn, MCWeightedItemStack[] output, int progressPerTick, int maxProgress, int rfPerTick) {
        ChancedItemStackList list = ChancedItemStackList.of(Arrays.stream(output)
                .map((stack) -> ChancedItemStack.of(stack.getItemStack().getInternal(), stack.getWeight()))
                .toArray(ChancedItemStack[]::new));

        return new BotariumRecipe(new ResourceLocation("crafttweaker", name), cropIn.asVanillaIngredient(), inputFluid.getImmutableInternal(),
                soilIn.asVanillaIngredient(), list, progressPerTick, maxProgress, rfPerTick);
    }

    @Override
    public IRecipeType<BotariumRecipe> getRecipeType() {
        return RecipeRegistry.BOTARIUM_RECIPE_TYPE;
    }
}
