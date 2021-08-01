package software.bernie.techarium.display.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.util.Vector2i;

public class DrawableWidget extends Widget {

    private final IDrawable drawable;

    public DrawableWidget(IDrawable drawable,int xIn, int yIn, int widthIn, int heightIn, ITextComponent msg) {
        super(xIn, yIn, widthIn, heightIn, msg);
        this.drawable = drawable;
    }

    public void setDrawOffset(Vector2i offset) {
        drawable.updateOffset(offset);
    }

    public Vector2i getDrawOffset() {
        return drawable.getDrawOffset();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        drawable.draw(matrixStack, x, y);
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return false;
    }
}
