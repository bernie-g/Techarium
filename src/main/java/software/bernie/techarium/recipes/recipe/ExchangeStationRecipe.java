package software.bernie.techarium.recipes.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.recipes.AbstractMachineRecipe;
import software.bernie.techarium.registry.RecipeRegistry;


public class ExchangeStationRecipe extends AbstractMachineRecipe {


    public ExchangeStationRecipe(ResourceLocation id, int tickRate, int maxProgress, int energyCost) {
        super(id, RecipeRegistry.BOTARIUM_RECIPE_TYPE, tickRate, maxProgress, energyCost);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.BOTARIUM_SERIALIZER.get();
    }


    @Override
    protected TechariumRecipeBuilder.Result getResult(ResourceLocation id)
    {
        return null;
    }
}
