package software.bernie.techarium.display.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.client.screen.draw.IDrawable;

public abstract class DrawableContainerScreen<CONTAINER extends Container> extends ContainerScreen<CONTAINER> {

    public DrawableContainerScreen(CONTAINER screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        titleLabelX = Integer.MIN_VALUE;
        inventoryLabelX = Integer.MIN_VALUE;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        getBackground().draw(getGuiLeft(), getGuiTop(), getXSize(), getYSize());
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        super.renderLabels(matrixStack, x, y);
        int xCenter = (width - imageWidth) / 2;
        int yCenter = (height - imageHeight) / 2;
        renderCustomToolTips(matrixStack, x,y,xCenter, yCenter);
    }

    protected void renderCustomToolTips(MatrixStack matrixStack, int mouseX, int mouseY, int xCenter, int yCenter) {
        renderTooltip(matrixStack, mouseX - xCenter, mouseY - yCenter);
    }

    abstract protected IDrawable getBackground();
}
