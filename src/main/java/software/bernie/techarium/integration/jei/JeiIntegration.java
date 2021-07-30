package software.bernie.techarium.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.Internal;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.gui.elements.DrawableSprite;
import mezz.jei.gui.elements.GuiIconButtonSmall;
import mezz.jei.gui.recipes.IRecipeGuiLogic;
import mezz.jei.gui.recipes.RecipeTransferButton;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.gui.textures.JeiSpriteUploader;
import mezz.jei.gui.textures.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.integration.ClientIntegration;
import software.bernie.techarium.integration.jei.category.BaseRecipeCategory;
import software.bernie.techarium.util.LogCache;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class JeiIntegration extends ClientIntegration {

    @Nullable
    private static Field recipesGuiLogicField;
    @Nullable
    private static Method recipesGuiParentContainerMethod;
    @Nullable
    private static Field iconField;
    @Nullable
    private static Field spriteUploaderField;

    //don't manipulate a button twice
    private static WeakHashMap<RecipeTransferButton, Void> manipulatedButtons = new WeakHashMap<>();
    static {
        try {
            recipesGuiLogicField = RecipesGui.class.getDeclaredField("logic");
            recipesGuiLogicField.setAccessible(true);
            recipesGuiParentContainerMethod = RecipesGui.class.getDeclaredMethod("getParentContainer");
            recipesGuiParentContainerMethod.setAccessible(true);
            iconField = GuiIconButtonSmall.class.getDeclaredField("icon");
            iconField.setAccessible(true);
            spriteUploaderField = Textures.class.getDeclaredField("spriteUploader");
            spriteUploaderField.setAccessible(true);

        } catch (NoSuchFieldException | NoSuchMethodException e) {
            LogCache.getLogger(JeiIntegration.class).warn(e);
        }
    }

    public JeiIntegration(String modID) {
        super(modID);
    }

    public void draw(Screen screen) {
        if (!isJeiGui(screen))
            return;
        RecipesGui recipesGui = (RecipesGui) screen;
        IRecipeCategory<?> category = getRecipeCategory(recipesGui);
        if (!(category instanceof BaseRecipeCategory))
            return;
        BaseRecipeCategory<?> baseRecipeCategory = (BaseRecipeCategory<?>) category;
        List<RecipeTransferButton> recipeWidgets = new ArrayList<>();
        for (Widget widget: recipesGui.buttons) {
            if (widget instanceof RecipeTransferButton) {
                recipeWidgets.add((RecipeTransferButton)widget);
            }
        }
        if (recipeWidgets.isEmpty())
            return;
        Container container = getContainer(recipesGui);
        if (!(container instanceof AutomaticContainer))
            return;
        AutomaticContainer automaticContainer = (AutomaticContainer) container;
        if (automaticContainer.getTile().getRecipeClass() == category.getRecipeClass()) {
            for (RecipeTransferButton recipeWidget: recipeWidgets) {
                createRecipeWidget(recipeWidget, baseRecipeCategory);
            }
        } else {
            for (RecipeTransferButton recipeWidget: recipeWidgets) {
                recipeWidget.visible = false;
            }
        }
    }

    public static void createRecipeWidget(RecipeTransferButton widget, BaseRecipeCategory category) {
        if (manipulatedButtons.containsKey(widget))
            return;
        IDrawable icon = getIcon(widget);
        if (icon != null && iconField != null) {
            try {
                iconField.set(widget, icon);
            } catch (IllegalAccessException e) {
                LogCache.getLogger(JeiIntegration.class).warn(e);
            }
        }
    }

    @Nullable
    public static IRecipeCategory<?> getRecipeCategory(RecipesGui recipesGui) {
        if (recipesGuiLogicField == null)
            return null;
        try {
            IRecipeGuiLogic logic = (IRecipeGuiLogic) recipesGuiLogicField.get(recipesGui);
            return logic.getSelectedRecipeCategory();
        } catch (IllegalAccessException e) {
            LogCache.getLogger(JeiIntegration.class).warn(e);
        }
        return null;
    }

    @Nullable
    public static Container getContainer(RecipesGui recipesGui) {
        if (recipesGuiParentContainerMethod == null)
            return null;
        try {
            return (Container) recipesGuiParentContainerMethod.invoke(recipesGui);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LogCache.getLogger(JeiIntegration.class).warn(e);
        }
        return null;
    }

    @Nullable
    private static IDrawable getIcon(RecipeTransferButton button) {
        ResourceLocation locationActive = Techarium.rl("textures/gui/jei/recipe_button_active.png");
        ResourceLocation locationInactive = Techarium.rl("textures/gui/jei/recipe_button_inactive.png");
        try {
            if (spriteUploaderField != null) {
                JeiSpriteUploader spriteUploader = (JeiSpriteUploader) (spriteUploaderField.get(Internal.getTextures()));
                 return new CustomsSprite(spriteUploader, locationActive, locationInactive, 256, 256, isMouseOver(button));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Supplier<Boolean> isMouseOver(RecipeTransferButton button) {

        return () -> {
            int mouseX = (int)(Minecraft.getInstance().mouseHandler.xpos() * (double)Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double)Minecraft.getInstance().getWindow().getScreenWidth());
            int mouseY = (int)(Minecraft.getInstance().mouseHandler.ypos() * (double)Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double)Minecraft.getInstance().getWindow().getScreenHeight());
            return button.isMouseOver(mouseX, mouseY);
        };
    }
    
    public static boolean isJeiGui(Screen screen) {
        return screen instanceof RecipesGui;
    }

    private static class CustomsSprite extends DrawableSprite {

        private final ResourceLocation locationActive;
        private final ResourceLocation locationInactive;
        private final int width;
        private final int height;

        private final Supplier<Boolean> isHovered;

        public CustomsSprite(JeiSpriteUploader spriteUploader, ResourceLocation locationActive, ResourceLocation locationInactive, int width, int height, Supplier<Boolean> isHovered) {
            super(spriteUploader, locationActive, 13, 13);
            this.locationActive = locationActive;
            this.locationInactive = locationInactive;
            this.width = width;
            this.height = height;
            this.isHovered = isHovered;
        }

        @Override
        public void draw(MatrixStack matrixStack, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {

            Minecraft.getInstance().getTextureManager().bind(isHovered.get() ? locationInactive : locationActive);
            AbstractGui.blit(matrixStack, xOffset, yOffset, 0, 0, this.width, this.height, this.width, this.height);

        }
    }
}
