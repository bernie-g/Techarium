package software.bernie.techarium.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class BlitUtil {

    public static void blit(int x, int y, int z, int width, int height, float minU, float maxU, float minV, float maxV) {
        blitPrivate(x, x + width, y, y + height, z, minU, maxU, minV, maxV);

    }
    public static void blit(int x, int y, int z, int width, int height, TextureAtlasSprite sprite) {
        blitPrivate(x, x + width, y, y + height, z, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(),
                      sprite.getMaxV());
    }

    public static void blit(int x, int y, int z, float u, float v, int width, int height, int vScale, int uScale) {
        blitPrivate(x, x + width, y, y + height, z, width, height, u, v, uScale, vScale);
    }

    public static void blit(int x, int y, int width, int height, float minU, float minV, int maxU, int maxV, int uScale,
                            int vScale) {
        blitPrivate(x, x + width, y, y + height, 0, maxU, maxV, minU, minV, uScale, vScale);
    }

    public static void blit(int x, int y, float minU, float minV, int width, int height, int uScale, int vScale) {
        blit(x, y, width, height, minU, minV, width, height, uScale, vScale);
    }

    private static void blitPrivate(int x0, int x1, int y0, int y1, int z, int maxU, int maxV, float minU, float minV,
                             int uScale, int vScale) {
        blitPrivate(x0, x1, y0, y1, z, (minU + 0.0F) / (float) uScale, (minU + (float) maxU) / (float) uScale,
                      (minV + 0.0F) / (float) vScale, (minV + (float) maxV) / (float) vScale);
    }

    public static void blitPrivate(int x0, int x1, int y1, int y0, int z, float minU, float maxU, float minV,
                                   float maxV) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x0, y0, z).tex(minU, maxV).endVertex();
        bufferbuilder.pos(x1, y0, z).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(x1, y1, z).tex(maxU, minV).endVertex();
        bufferbuilder.pos(x0, y1, z).tex(minU, minV).endVertex();
        tessellator.draw();
    }

}
