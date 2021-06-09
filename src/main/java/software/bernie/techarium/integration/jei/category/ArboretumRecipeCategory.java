package software.bernie.techarium.integration.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.recipes.recipe.ArboretumRecipe;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.BlockTileRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArboretumRecipeCategory extends BaseRecipeCategory<ArboretumRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(Techarium.ModID, "arboretum");

	public ArboretumRecipeCategory(IRecipeCategoryRegistration registration) {
		super(UID, registration, ArboretumRecipe.class, BlockTileRegistry.ARBORETUM.getBlock().getName(), BlockTileRegistry.ARBORETUM.getItem(), new ResourceLocation(Techarium.ModID, "textures/gui/arboretum/arboretum.png"),0,0,256,256);
	}

	@Override
	public IDrawable getIcon()
	{
		return registration.getJeiHelpers().getGuiHelper().createDrawableIngredient(new ItemStack(BlockTileRegistry.ARBORETUM.getItem()));
	}

	@Override
	public void setIngredients(ArboretumRecipe recipe, IIngredients ingredients) {

		List<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(Arrays.asList(recipe.getCropType().getItems()));
		inputs.add(Arrays.asList(recipe.getSoilIn().getItems()));
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setInput(VanillaTypes.FLUID, recipe.getFluidIn());
		
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutput().dissolve());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ArboretumRecipe recipe, IIngredients ingredients)
	{
		IGuiFluidStackGroup fluidStackGroup = layout.getFluidStacks();
		fluidStackGroup.init(0, true, 15, 10);
		fluidStackGroup.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));

		IGuiItemStackGroup group = layout.getItemStacks();
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		group.init(0, true, 0, 4);
		group.init(1, true, 30, 4);
		group.init(2, false, 60, 4);
		group.set(0, inputs.get(0));
		group.set(1, inputs.get(1));
		group.set(2, outputs.get(0));
	}
}
