package software.bernie.techarium.integration.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.BlockTileRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BotariumRecipeCategory implements IRecipeCategory<BotariumRecipe>
{
	private IRecipeCategoryRegistration registration;

	public BotariumRecipeCategory(IRecipeCategoryRegistration registration)
	{
		this.registration = registration;
	}

	@Override
	public ResourceLocation getUid()
	{
		return new ResourceLocation(Techarium.ModID, "botarium");
	}

	@Override
	public Class<BotariumRecipe> getRecipeClass()
	{
		return BotariumRecipe.class;
	}

	@Override
	public String getTitle()
	{
		return new TranslationTextComponent("techarium.botarium.recipe_catory.name").getString();
	}

	@Override
	public IDrawable getBackground()
	{
		return null;
	}


	@Override
	public IDrawable getIcon()
	{
		return registration.getJeiHelpers().getGuiHelper().createDrawableIngredient(BlockTileRegistry.BOTARIUM.get());
	}

	@Override
	public void setIngredients(BotariumRecipe recipe, IIngredients ingredients)
	{
		List<ItemStack> validIngredients = new ArrayList<>();

		List<Item> allItems = new ArrayList<>();
		ForgeRegistries.BLOCKS.getValues().forEach(block -> allItems.add(Item.getItemFromBlock(block)));
		allItems.addAll(ForgeRegistries.ITEMS.getValues());

		for (Item item : allItems)
		{
			ItemStack stack = new ItemStack(item);
			if (recipe.getCropType().test(stack))
			{
				validIngredients.add(stack);
			}
		}

		ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(validIngredients));
		ingredients.setInput(VanillaTypes.FLUID, recipe.getFluidIn());
		ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(Arrays.asList(recipe.getSoilIn().getMatchingStacks())));
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, BotariumRecipe recipe, IIngredients ingredients)
	{
		IGuiFluidStackGroup fluidStackGroup = recipeLayout.getFluidStacks();
		fluidStackGroup.init(0, true, 15, 10);
		fluidStackGroup.set(0, recipe.getFluidIn());

		IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
		itemStackGroup.init(0, true, 40, 30);
		itemStackGroup.init(1, true, 70, 30);

		itemStackGroup.set(0, Arrays.asList(recipe.getSoilIn().getMatchingStacks()));
		List<ItemStack> inputs = ingredients.getInputs(VanillaTypes.ITEM).get(0);
		List<ItemStack> outputs = new ArrayList<>();

		for(ItemStack input : inputs)
		{

		}
	}
}
