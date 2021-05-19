package software.bernie.techarium.integration.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class BaseRecipeCategory<T extends IRecipe> implements IRecipeCategory<T>
{
	private final ResourceLocation id;
	protected IRecipeCategoryRegistration registration;
	private final Class<? extends T> recipeClass;
	private final ITextComponent title;
	private final Object iconItemBlock;
	private final IDrawable drawable;
	protected IDrawable fluidOverlayLarge;
	protected IDrawable fluidOverlaySmall;

	public BaseRecipeCategory(ResourceLocation id, IRecipeCategoryRegistration registration, Class<? extends T> recipeClass, ITextComponent title, Object iconItemBlock, ResourceLocation background, int xOffset, int yOffset, int width, int height)
	{
		this.id = id;
		this.registration = registration;
		this.recipeClass = recipeClass;
		this.title = title;
		this.iconItemBlock = iconItemBlock;
		this.drawable = registration.getJeiHelpers().getGuiHelper().createDrawable(background, xOffset, yOffset, width, height);

	}

	@Override
	public ResourceLocation getUid()
	{
		return id;
	}

	@Override
	public Class<? extends T> getRecipeClass()
	{
		return recipeClass;
	}

	@Override
	public String getTitle()
	{
		return title.getString();
	}

	@Override
	public IDrawable getBackground() {
		return drawable;
	}

	@Override
	public IDrawable getIcon()
	{
		if (iconItemBlock == null) return null;
		return registration.getJeiHelpers().getGuiHelper().createDrawableIngredient(this.iconItemBlock);
	}


	protected void addGuiElements()
	{
	}
}
