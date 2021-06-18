package software.bernie.techarium.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.openzen.zencode.java.ZenCodeType;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.recipes.recipe.ExchangeStationRecipe;
import software.bernie.techarium.registry.RecipeRegistry;

@ZenRegister
@ZenCodeType.Name("mods.techarium.ExchangeStation")
public class ExchangeStationRecipeManager implements IRecipeManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack input, IItemStack output) {
        name = fixRecipeName(name);
        ExchangeStationRecipe recipe = createExchangeStationRecipe(name, input, output);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }

    @NotNull
    private ExchangeStationRecipe createExchangeStationRecipe(String name, IItemStack input, IItemStack output) {
        return new ExchangeStationRecipe(new ResourceLocation("crafttweaker", name), input.getImmutableInternal(), output.getImmutableInternal());
    }

    @Override
    public IRecipeType<ExchangeStationRecipe> getRecipeType() {
        return RecipeRegistry.EXCHANGE_STATION_RECIPE_TYPE;
    }
}
