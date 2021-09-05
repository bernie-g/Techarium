package software.bernie.techarium.display.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.util.Vector2i;


public class ScrollBarWidget extends Widget {
    private static final int WIDTH = 8;
    private static final Vector2i SCROLLER_DIMENSION_VERTICAL = new Vector2i(6,15);
    private static final Vector2i SCROLLER_POSITION_VERTICAL = new Vector2i(1,3);
    private static final Vector2i SCROLLER_DIMENSION_HORIZONTAL = new Vector2i(15,6);
    private static final Vector2i SCROLLER_POSITION_HORIZONTAL = new Vector2i(17,1);
    private static final ResourceLocation texture = new ResourceLocation(Techarium.MOD_ID, "textures/gui/scrollbar.png");

    private final boolean vertical;

    @Getter
    @Setter
    private float scrollPosition;
    private boolean isScrolling;

    private final boolean renderBG;

    public ScrollBarWidget(Vector2i pos, int length, boolean vertical, boolean renderBG) {
        super(pos.getX(), pos.getY(), dimensionXOf(length, vertical), dimensionYOf(length, vertical), StringTextComponent.EMPTY);
        this.vertical = vertical;
        this.renderBG = renderBG;
    }

    static int dimensionXOf(int length, boolean vertical) {
        if (vertical) return WIDTH;
        return length;
    }

    static int dimensionYOf(int length, boolean vertical) {
        if (vertical) return length;
        return WIDTH;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(texture);
        int length = vertical ? height : width;
        matrixStack.pushPose();
        matrixStack.translate(x,y,0);
        if (vertical) {
            if (renderBG) {
                blit(matrixStack, 0, 0, 0, 0, WIDTH, 1);
                for (int y = 1; y < length - 1; y++) {
                    blit(matrixStack, 0, y, 0, 1, WIDTH, 1);
                }
                blit(matrixStack, 0, length - 1, 0, 2, WIDTH, 1);
            }
            Vector2i scrollerPosition = new Vector2i(1, (int)((length - 17) * scrollPosition) + 1);
            blit(matrixStack, scrollerPosition.getX(), scrollerPosition.getY(), SCROLLER_POSITION_VERTICAL.getX(), SCROLLER_POSITION_VERTICAL.getY(), SCROLLER_DIMENSION_VERTICAL.getX(), SCROLLER_DIMENSION_VERTICAL.getY());
        } else{
            if (renderBG) {
                blit(matrixStack, 0, 0, 14, 0, 1, WIDTH);
                for (int x = 1; x < length - 1; x++) {
                    blit(matrixStack, x, 0, 15, 0, 1, WIDTH);
                }
                blit(matrixStack, length - 1, 0, 16, 0, 1, WIDTH);
            }
            Vector2i scrollerPosition = new Vector2i((int)((length - 17) * scrollPosition) + 1, 1);
            blit(matrixStack,scrollerPosition.getX(), scrollerPosition.getY(), SCROLLER_POSITION_HORIZONTAL.getX(), SCROLLER_POSITION_HORIZONTAL.getY(), SCROLLER_DIMENSION_HORIZONTAL.getX(),SCROLLER_DIMENSION_HORIZONTAL.getY());
        }
        matrixStack.popPose();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        Vector2i relativeMouse = new Vector2i((int) mouseX, (int) mouseY);

        isScrolling = true;
        handleScroll(relativeMouse);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if(isScrolling) {
            isScrolling = false;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scrollPosition = (float) MathHelper.clamp(scrollPosition - delta/10, 0, 1);
        return true;
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        Vector2i relativeMouse = new Vector2i((int) (mouseX + dragX), (int) (mouseY + dragY));
        if (isScrolling) {
            handleScroll(relativeMouse);
        }
    }

    private void handleScroll(Vector2i mouse) {
        handleScroll(vertical ? mouse.getY() : mouse.getX());
    }
    private void handleScroll(int heightOrWidth) {
        double scroll;
        double length;
        double scrollPos;
        if(vertical) {
            length = height - 17d;
            scrollPos = heightOrWidth - 7d - y;
        }else {
            length = width - 17d;
            scrollPos = heightOrWidth - 7d - x;
        }

        scroll = scrollPos / length;

        scrollPosition = (float) MathHelper.clamp(scroll, 0, 1);
        isScrolling = true;
    }
}
