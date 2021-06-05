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
import software.bernie.techarium.integration.jei.category.BotariumRecipeCategory;
import software.bernie.techarium.integration.jei.panel.PanelBoundHandler;
import software.bernie.techarium.machine.screen.AutomaticContainerScreen;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.BlockTileRegistry;
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
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(BlockTileRegistry.BOTARIUM.getBlock()), BotariumRecipeCategory.UID);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ClientWorld world = Minecraft.getInstance().level;
		if (world != null) {
			List<BotariumRecipe> recipes = world.getRecipeManager().getAllRecipesFor(RecipeRegistry.BOTARIUM_RECIPE_TYPE);
			registration.addRecipes(recipes, BotariumRecipeCategory.UID);
		}

		registration.addIngredientInfo(new ItemStack(BlockTileRegistry.BOTARIUM.getBlock()), VanillaTypes.ITEM, "jei.techarium.botarium.description");
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiContainerHandler(AutomaticContainerScreen.class, new PanelBoundHandler());
	}
}
