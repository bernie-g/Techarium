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
import software.bernie.techarium.recipe.recipe.ArboretumRecipe;
import software.bernie.techarium.registry.BlockRegistry;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import software.bernie.techarium.util.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ArboretumRecipeCategory extends BaseRecipeCategory<ArboretumRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(Techarium.ModID, "arboretum");

	public ArboretumRecipeCategory(IRecipeCategoryRegistration registration) {
		super(UID, registration, ArboretumRecipe.class, BlockRegistry.ARBORETUM.getBlock().getName(), BlockRegistry.ARBORETUM.getItem(), Techarium.rl("textures/gui/jei/arboretum.png"),new Vector2i(0,0),new Vector2i(176,79));
	}

	@Override
	public IDrawable getIcon() {
		return registration.getJeiHelpers().getGuiHelper().createDrawableIngredient(new ItemStack(BlockRegistry.ARBORETUM.getItem()));
	}

	/**
	 * @return the offset from the default jei Button position to our position. to get higher/left use higher values
	 */
	@Override
	public Vector2i getJeiButtonPosition() {
		return new Vector2i(154,53);
	}

	@Override
	public void setIngredients(ArboretumRecipe recipe, IIngredients ingredients) {

		List<List<ItemStack>> inputs = new ArrayList<>();

		inputs.add(Arrays.asList(recipe.getSoilIn().getItems()));
		inputs.add(Arrays.asList(recipe.getCropType().getItems()));
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setInput(VanillaTypes.FLUID, recipe.getFluidIn());
		
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutput().dissolve());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ArboretumRecipe recipe, IIngredients ingredients) {
		super.setRecipe(layout, recipe, ingredients);

		IGuiFluidStackGroup fluidStacks = layout.getFluidStacks();
		addFluid(recipe.getFluidIn(), 10000, new Vector2i(29, 22), fluidStacks, true, 0);
		addEnergyWidget(recipe, recipe.getMaxProgress() * recipe.getProgressPerTick() * recipe.getRfPerTick(), 10000, recipe.getRfPerTick(), 10, 23);
		IGuiItemStackGroup group = layout.getItemStacks();
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		group.init(0, true, 53, 51);
		group.init(1, true, 53, 30);
		group.set(0, inputs.get(0));
		group.set(1, inputs.get(1));
		for (int i = 0; i < Math.min(3, outputs.size()); i++) {
			addChancedItemStack(recipe.getOutput().getStackList().get(i), new Vector2i(102 + 21*i, 30), group, layout, 2 + i);
		}
	}
}
