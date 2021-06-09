package software.bernie.techarium.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.integration.jei.category.ArboretumRecipeCategory;
import software.bernie.techarium.integration.jei.category.BotariumRecipeCategory;
import software.bernie.techarium.integration.jei.panel.PanelBoundHandler;
import software.bernie.techarium.machine.screen.AutomaticContainerScreen;
import software.bernie.techarium.recipes.recipe.ArboretumRecipe;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.BlockTileRegistry;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.registry.RecipeRegistry;

import java.util.List;

@JeiPlugin
public class TechariumJEIPlugin implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return new ResourceLocation(Techarium.ModID, "techarium_jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new BotariumRecipeCategory(registration));
		registration.addRecipeCategories(new ArboretumRecipeCategory(registration));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(BlockTileRegistry.BOTARIUM.getBlock()), BotariumRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(BlockTileRegistry.ARBORETUM.getBlock()), ArboretumRecipeCategory.UID);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ClientWorld world = Minecraft.getInstance().level;
		if (world != null) {
			List<BotariumRecipe> recipesBotarium = world.getRecipeManager().getAllRecipesFor(RecipeRegistry.BOTARIUM_RECIPE_TYPE);
			registration.addRecipes(recipesBotarium, BotariumRecipeCategory.UID);

			List<ArboretumRecipe> recipesArboretum = world.getRecipeManager().getAllRecipesFor(RecipeRegistry.ARBORETUM_RECIPE_TYPE);
			registration.addRecipes(recipesArboretum, ArboretumRecipeCategory.UID);
		}

		registration.addIngredientInfo(new ItemStack(BlockTileRegistry.BOTARIUM.getBlock()), VanillaTypes.ITEM, LangRegistry.jeiBotariumDescription.getKey());
		registration.addIngredientInfo(new ItemStack(BlockTileRegistry.ARBORETUM.getBlock()), VanillaTypes.ITEM, LangRegistry.jeiArboretumDescription.getKey());
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiContainerHandler(AutomaticContainerScreen.class, new PanelBoundHandler());
	}
}
