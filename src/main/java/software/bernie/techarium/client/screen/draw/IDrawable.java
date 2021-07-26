package software.bernie.techarium.client.screen.draw;

import com.mojang.blaze3d.matrix.MatrixStack;
import software.bernie.techarium.util.Vector2i;

import java.util.function.Supplier;

public interface IDrawable {

    default void draw(MatrixStack stack, Vector2i drawPos) {
        drawPartial(stack,drawPos, getSize(), getTexturePos());
    }

    default void draw(MatrixStack stack, int x, int y) {
        draw(stack, new Vector2i(x,y));
    }

    Vector2i getTexturePos();

    Vector2i getSize();

    void updateOffset(Supplier<Vector2i> offset);

    Vector2i getDrawOffset();

    void drawPartial(MatrixStack stack, Vector2i drawPos, Vector2i size, Vector2i texturePos);

}