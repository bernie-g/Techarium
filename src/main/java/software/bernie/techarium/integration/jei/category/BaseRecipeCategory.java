package software.bernie.techarium.integration.jei.category;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.client.screen.draw.GuiAddonTextures;
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.display.screen.widget.EnergyAutoWidget;
import software.bernie.techarium.display.screen.widget.TankWidget;
import software.bernie.techarium.integration.jei.RecipeDisplayWidget;
import software.bernie.techarium.integration.jei.transferhandler.TechariumTransferInfo;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.machine.interfaces.ITooltipAddon;
import software.bernie.techarium.machine.interfaces.ITooltipProvider;
import software.bernie.techarium.util.loot.ChancedItemStack;
import software.bernie.techarium.util.Vector2i;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseRecipeCategory<T extends IRecipe<?>> implements IRecipeCategory<T> {

	public static final Map<Class<? extends BaseRecipeCategory>, BaseRecipeCategory> INSTANCES = new HashMap<>();
	private final ResourceLocation id;
	protected IRecipeCategoryRegistration registration;
	private final Class<? extends T> recipeClass;
	private final ITextComponent title;
	private final Object iconItemBlock;
	private final IDrawable background;
	//widgets like the EnergyAutoWidget
	private Map<T, List<Widget>> widgets = new HashMap<>();
	//needed to add our RecipeDisplayWidget once on the first rendering
	private Map<T, Boolean> isFirstDraw = new HashMap<>();
	//currently available RecipeDisplayWidgets. Are removed from this Map and the buttons on screen, once the recipe belonging to them is not rendered.
	private BiMap<T, RecipeDisplayWidget> recipeWidgets = HashBiMap.create();

	protected BaseRecipeCategory(ResourceLocation id, IRecipeCategoryRegistration registration, Class<? extends T> recipeClass, ITextComponent title, Object iconItemBlock, ResourceLocation background, Vector2i texturePos, Vector2i size) {
		INSTANCES.put(this.getClass(), this);
		this.id = id;
		this.registration = registration;
		this.recipeClass = recipeClass;
		this.title = title;
		this.iconItemBlock = iconItemBlock;
		this.background = registration.getJeiHelpers().getGuiHelper().createDrawable(background, texturePos.getX(), texturePos.getY(), size.getX(), size.getY());
	}

	/**
	 * draws the recipe. Draws all Widgets belonging to the recipe. If it's the first draw, it will add an RecipeDisplayWidget with current MatrixStack Information if needed.
	 * if the recipeDisplayWidget is present for this recipe, it will store the relative mousePosition and marks this recipeWidget as still used. In {@link RecipeDisplayWidget#renderButtonWithFeedback()} the validity is checked and the button is removed if necessary
	 */
	@Override
	public void draw(T recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		if (isFirstDraw.get(recipe)) {
			isFirstDraw.put(recipe, false);
			Container parentContainer = Minecraft.getInstance().player.containerMenu;
			if (parentContainer instanceof AutomaticContainer && TechariumTransferInfo.TRANSFER_INFOS.containsKey(getUid()) && TechariumTransferInfo.TRANSFER_INFOS.get(getUid()).canHandle((AutomaticContainer) parentContainer)) {
				RecipeDisplayWidget widget = new RecipeDisplayWidget(matrixStack, getJeiButtonPosition(), recipeWidgets);
				Minecraft.getInstance().screen.buttons.add(widget);
				recipeWidgets.put(recipe, widget);
			}
		}
		if (recipeWidgets.containsKey(recipe)) {
			recipeWidgets.get(recipe).setMouseX((int) mouseX);
			recipeWidgets.get(recipe).setMouseY((int) mouseY);
			recipeWidgets.get(recipe).setValid(true);
		}
		widgets.get(recipe).forEach(widget -> widget.render(matrixStack, (int)mouseX,(int)mouseY,0));
	}

	@Override
	public ResourceLocation getUid() {
		return id;
	}

	@Override
	public Class<? extends T> getRecipeClass() {
		return recipeClass;
	}

	@Override
	public String getTitle() {
		return title.getString();
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		if (iconItemBlock == null)
			return null;
		return registration.getJeiHelpers().getGuiHelper().createDrawableIngredient(this.iconItemBlock);
	}

	public abstract Vector2i getJeiButtonPosition();

	/**
	 * Prepares Maps for the new Recipe
	 */
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
		widgets.put(recipe, new ArrayList<>());
		isFirstDraw.put(recipe, true);
		recipeLayout.moveRecipeTransferButton(getJeiButtonPosition().getX(), getJeiButtonPosition().getY());
	}

	public void addGuiElement(T recipe, Widget widget) {
		this.widgets.get(recipe).add(widget);
	}

	/**
	 * Adds a chancedItemStack with displayInformation to the recipe
	 */
	protected void addChancedItemStack(ChancedItemStack stack, Vector2i pos, IGuiItemStackGroup group, IRecipeLayout layout, int index) {
		group.init(index, false, pos.getX(), pos.getY());
		group.set(index, stack.getStack());
		layout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (slotIndex == index) {
				tooltip.addAll(stack.getDisplayPercentages());
			}
		});

	}

	/**
	 * Adds a fluidStack to the recipe
	 */
	protected void addFluid(FluidStack fluidStack, int tankCapacity, Vector2i pos, IGuiFluidStackGroup group, boolean isInput, int index) {
		group.init(index, isInput, new FluidRenderer(tankCapacity), pos.getX(), pos.getY(), 14, 50, 0, 0);
		group.set(index, fluidStack);
	}

	/**
	 * Adds EnergyRequirement to the recipe
	 */
	protected void addEnergyWidget(T recipe, int amount, int capacity, int rfPerTick, int x, int y) {
		EnergyStorageAddon addon = new EnergyStorageAddon(capacity, x, y);
		addon.setLastDrained(rfPerTick);
		addon.forceSetEnergy(amount);
		EnergyAutoWidget energyWidget = new EnergyAutoWidget(addon);
		energyWidget.setDrawOffset(Vector2i.ZERO);
		addGuiElement(recipe,energyWidget);
	}

	@Override
	public List<ITextComponent> getTooltipStrings(T recipe, double mouseX, double mouseY) {
		List<ITextComponent> tooltip = new ArrayList<>();
		for (Widget widget: widgets.get(recipe)) {
			if (widget instanceof ITooltipProvider) {
				ITooltipAddon tooltipWidget = ((ITooltipProvider) widget).getTooltip();
				if (tooltipWidget.isHovering(0,0, (int)mouseX, (int)mouseY)) {
					tooltip.addAll(tooltipWidget.createToolTipMessage());
				}
			}
		}
		return tooltip;
	}

	private static class FluidRenderer implements IIngredientRenderer<FluidStack> {
		FluidTankAddon addon;
		TankWidget tankWidget;
		public FluidRenderer(int tankCapacity) {
			addon = new FluidTankAddon(new Vector2i(14, 50), "fluid", tankCapacity, 0, 0, fluid -> true);

			tankWidget = new TankWidget(addon, GuiAddonTextures.DEFAULT_FLUID_TANK, 0, 0, 14, 50, "");
			tankWidget.setDrawOffset(Vector2i.ZERO);
		}

		@Override
		public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable FluidStack fluidStack) {
			addon.setFluid(fluidStack);
			tankWidget.setDrawOffset(Vector2i.ZERO);
			tankWidget.x = xPosition;
			tankWidget.y = yPosition;
			tankWidget.render(matrixStack, Integer.MIN_VALUE, Integer.MIN_VALUE, 0);
		}

		@Override
		public List<ITextComponent> getTooltip(FluidStack ingredient, ITooltipFlag tooltipFlag) {
			return addon.createToolTipMessage();
		}
	}
}
