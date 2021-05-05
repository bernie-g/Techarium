package software.bernie.techarium.integration.jei.category;

import mekanism.client.jei.NOOPDrawable;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class BaseRecipeCategory<T extends IRecipe> implements IRecipeCategory<T>
{
	private final ResourceLocation id;
	private IRecipeCategoryRegistration registration;
	private final Class<? extends T> recipeClass;
	private final String unlocalizedTitle;
	private final Object iconItemBlock;
	private final IDrawable drawable;
	private final int xOffset;
	private final int yOffset;
	private final int width;
	private final int height;
	protected IDrawable fluidOverlayLarge;
	protected IDrawable fluidOverlaySmall;

	public BaseRecipeCategory(ResourceLocation id, IRecipeCategoryRegistration registration, Class<? extends T> recipeClass, String unlocalizedTitle, Object iconItemBlock, IDrawable drawable, int xOffset, int yOffset, int width, int height)
	{
		this.id = id;
		this.registration = registration;
		this.recipeClass = recipeClass;
		this.unlocalizedTitle = unlocalizedTitle;
		this.iconItemBlock = iconItemBlock;
		this.drawable = drawable;

		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
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
		return new TranslationTextComponent(unlocalizedTitle).getString();
	}

	@Override
	public IDrawable getBackground()
	{
		return new NOOPDrawable(this.width, this.height);
	}

	@Override
	public IDrawable getIcon()
	{
		return registration.getJeiHelpers().getGuiHelper().createDrawableIngredient(this.iconItemBlock);
	}


	protected void addGuiElements()
	{
	}
}
