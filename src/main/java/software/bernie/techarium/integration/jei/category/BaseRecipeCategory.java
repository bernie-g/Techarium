package software.bernie.techarium.integration.jei.category;

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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.client.screen.draw.GuiAddonTextures;
import software.bernie.techarium.display.screen.widget.EnergyAutoWidget;
import software.bernie.techarium.display.screen.widget.TankWidget;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.machine.interfaces.ITooltipAddon;
import software.bernie.techarium.machine.interfaces.ITooltipProvider;
import software.bernie.techarium.util.ChancedItemStack;
import software.bernie.techarium.util.Vector2i;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseRecipeCategory<T extends IRecipe<?>> implements IRecipeCategory<T> {
	private final ResourceLocation id;
	protected IRecipeCategoryRegistration registration;
	private final Class<? extends T> recipeClass;
	private final ITextComponent title;
	private final Object iconItemBlock;
	private final IDrawable background;
	private List<Widget> widgets = new ArrayList<>();

	protected BaseRecipeCategory(ResourceLocation id, IRecipeCategoryRegistration registration, Class<? extends T> recipeClass, ITextComponent title, Object iconItemBlock, ResourceLocation background, Vector2i texturePos, Vector2i size) {
		this.id = id;
		this.registration = registration;
		this.recipeClass = recipeClass;
		this.title = title;
		this.iconItemBlock = iconItemBlock;
		this.background = registration.getJeiHelpers().getGuiHelper().createDrawable(background, texturePos.getX(), texturePos.getY(), size.getX(), size.getY());
	}

	@Override
	public void draw(T recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		ModIntegrations.getJEI().ifPresent(jeiIntegration -> jeiIntegration.draw(Minecraft.getInstance().screen));
		widgets.forEach(widget -> widget.render(matrixStack, (int)mouseX,(int)mouseY,0));
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

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
		widgets.clear();
	}

	public void addGuiElement(Widget widget) {
		this.widgets.add(widget);
	}

	protected void addChancedItemStack(ChancedItemStack stack, int x, int y, IGuiItemStackGroup group, IRecipeLayout layout, int index) {
		group.init(index, false, x, y);
		group.set(index, stack.getStack());
		layout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (slotIndex == index) {
				tooltip.addAll(stack.getDisplayPercentages());
			}
		});

	}

	protected void addFluid(FluidStack fluidStack, int tankCapacity, int x, int y, IGuiFluidStackGroup group, boolean isInput, int index) {
		group.init(index, isInput, new FluidRenderer(tankCapacity), x, y, 14, 50, 0, 0);
		group.set(index, fluidStack);
	}

	protected void addEnergyWidget(int amount, int capacity, int rfPerTick, int x, int y) {
		EnergyStorageAddon addon = new EnergyStorageAddon(capacity, x, y);
		addon.setLastDrained(rfPerTick);
		addon.forceSetEnergy(amount);
		EnergyAutoWidget energyWidget = new EnergyAutoWidget(addon);
		energyWidget.setDrawOffset(() -> Vector2i.ZERO);
		addGuiElement(energyWidget);
	}

	@Override
	public List<ITextComponent> getTooltipStrings(T recipe, double mouseX, double mouseY) {
		List<ITextComponent> tooltip = new ArrayList<>();
		for (Widget widget: widgets) {
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
			tankWidget.setDrawOffset(() -> Vector2i.ZERO);
		}

		@Override
		public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable FluidStack fluidStack) {
			addon.setFluid(fluidStack);
			tankWidget.setDrawOffset(() -> Vector2i.ZERO);
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
