package software.bernie.techarium.client.screen.draw;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.util.Vector2i;
import java.util.function.Supplier;

public class UiTexture {
    private final ResourceLocation texture;
    private final int x;
    private final int y;

    public UiTexture(ResourceLocation texture, int x, int y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
    }

    public void bindTexture() {
        Minecraft.getInstance().textureManager.bind(texture);
    }

    public IDrawable getFullArea() {
        return new Area(new Vector2i(0,0), new Vector2i(x, y));
    }
    public IDrawable getArea(Vector2i texturePos, Vector2i size) {
        return new Area(texturePos, size);
    }


    @RequiredArgsConstructor
    private class Area implements IDrawable {

        private final Vector2i texturePos;
        private final Vector2i size;
        @Setter
        private Supplier<Vector2i> drawOffset = Vector2i::new;
        @Override
        public Vector2i getTexturePos() {
            return texturePos;
        }

        @Override
        public Vector2i getSize() {
            return size;
        }

        @Override
        public void updateOffset(Supplier<Vector2i> offset) {
            drawOffset = offset;
        }

        @Override
        public Vector2i getDrawOffset() {
            return drawOffset.get();
        }

        @Override
        public void drawPartial(MatrixStack stack, Vector2i drawPos, Vector2i size, Vector2i texturePos) {
            bindTexture();
            Screen screen = Minecraft.getInstance().screen;
            drawPos = drawPos.add(drawOffset.get());
            screen.blit(stack, drawPos.getX(), drawPos.getY(), texturePos.getX(), texturePos.getY(),size.getX(), size.getY());
        }
    }
}