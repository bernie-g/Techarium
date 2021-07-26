package software.bernie.techarium.integration.jei.category;

import mezz.jei.api.MethodsReturnNonnullByDefault;
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
import software.bernie.techarium.recipe.recipe.BotariumRecipe;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.util.Vector2i;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BotariumRecipeCategory extends BaseRecipeCategory<BotariumRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(Techarium.ModID, "botarium");

	public BotariumRecipeCategory(IRecipeCategoryRegistration registration) {
		super(UID, registration, BotariumRecipe.class, BlockRegistry.BOTARIUM.getBlock().getName(), BlockRegistry.BOTARIUM.getItem(), Techarium.rl("textures/gui/jei/botarium.png"),0,0,176,79);
	}

	@Override
	public IDrawable getIcon() {
		return registration.getJeiHelpers().getGuiHelper().createDrawableIngredient(new ItemStack(BlockRegistry.BOTARIUM.getItem()));
	}

	@Override
	public Vector2i getJeiButtonPosition() {
		return new Vector2i(28,13);
	}

	@Override
	public void setIngredients(BotariumRecipe recipe, IIngredients ingredients) {

		List<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(Arrays.asList(recipe.getSoilIn().getItems()));
		inputs.add(Arrays.asList(recipe.getCropType().getItems()));
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setInput(VanillaTypes.FLUID, recipe.getFluidIn());

		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutput().getCachedOutput());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, BotariumRecipe recipe, IIngredients ingredients) {
		super.setRecipe(layout, recipe, ingredients);

		IGuiFluidStackGroup fluidStacks = layout.getFluidStacks();
		addFluid(recipe.getFluidIn(), 10000, 29, 22, fluidStacks, true, 0);
		addEnergyWidget(recipe.getMaxProgress() * recipe.getProgressPerTick() * recipe.getRfPerTick(), 10000, recipe.getRfPerTick(), 10, 23);
		IGuiItemStackGroup group = layout.getItemStacks();
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		group.init(0, true, 53, 51);
		group.init(1, true, 53, 30);
		group.set(0, inputs.get(0));
		group.set(1, inputs.get(1));
		for (int i = 0; i < Math.min(3, outputs.size()); i++) {
			addChancedItemStack(recipe.getOutput().getStackList().get(i), 102 + 21*i, 30, group, layout, 2 + i);
		}
	}
}
