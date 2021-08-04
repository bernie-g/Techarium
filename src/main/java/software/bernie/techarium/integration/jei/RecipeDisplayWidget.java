package software.bernie.techarium.integration.jei;

import com.google.common.collect.BiMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.util.Vector2i;

public class RecipeDisplayWidget extends Widget {
    private static final ResourceLocation locationActive = Techarium.rl("textures/gui/jei/recipe_button_active.png");
    private static final ResourceLocation locationInactive = Techarium.rl("textures/gui/jei/recipe_button_inactive.png");

    private final MatrixStack referenceMatrix;
    @Setter
    private int mouseX;
    @Setter
    private int mouseY;

    @Setter
    private boolean isValid = true;
    @Getter
    private final BiMap<? extends IRecipe, RecipeDisplayWidget> removeFrom;
    public RecipeDisplayWidget(MatrixStack referenceMatrix, Vector2i pos, BiMap<? extends IRecipe, RecipeDisplayWidget> removeFrom) {
        super(pos.getX(), pos.getY(), 13, 13, StringTextComponent.EMPTY);
        this.referenceMatrix = new MatrixStack();
        this.referenceMatrix.last().pose().set(referenceMatrix.last().pose());
        this.removeFrom = removeFrom;
    }

    public boolean renderButtonWithFeedback() {
        boolean shouldBeRemoved = !isValid;
        isValid = false;
        Minecraft.getInstance().getTextureManager().bind(isMouseOver(this.mouseX, this.mouseY) ? locationInactive : locationActive);
        blit(referenceMatrix, x, y, 0, 0, this.width, this.height, this.width, this.height);
        return shouldBeRemoved;
    }
}
