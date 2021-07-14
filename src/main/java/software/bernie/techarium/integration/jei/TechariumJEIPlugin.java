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
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.display.screen.AutomaticContainerScreen;
import software.bernie.techarium.integration.jei.category.ArboretumRecipeCategory;
import software.bernie.techarium.integration.jei.category.BotariumRecipeCategory;
import software.bernie.techarium.integration.jei.panel.PanelBoundHandler;
import software.bernie.techarium.recipe.recipe.ArboretumRecipe;
import software.bernie.techarium.recipe.recipe.BotariumRecipe;
import software.bernie.techarium.registry.BlockRegistry;
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
		registration.addRecipeCatalyst(new ItemStack(BlockRegistry.BOTARIUM.getBlock()), BotariumRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ARBORETUM.getBlock()), ArboretumRecipeCategory.UID);
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

		registration.addIngredientInfo(new ItemStack(BlockRegistry.BOTARIUM.getBlock()), VanillaTypes.ITEM, LangRegistry.botariumDescription.getKey());
		registration.addIngredientInfo(new ItemStack(BlockRegistry.ARBORETUM.getBlock()), VanillaTypes.ITEM, LangRegistry.arboretumDescription.getKey());
		registration.addIngredientInfo(new ItemStack(BlockRegistry.EXCHANGE_STATION.getBlock()), VanillaTypes.ITEM, LangRegistry.exchangeDescription.getKey());
		registration.addIngredientInfo(new ItemStack(BlockRegistry.VOLTAIC_PILE.getBlock()), VanillaTypes.ITEM, LangRegistry.voltaicPileDescription.getKey());
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiContainerHandler(AutomaticContainerScreen.class, new PanelBoundHandler());
	}
}
