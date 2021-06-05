package software.bernie.techarium.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mekanism.client.render.MekanismRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

public class GuiUtils {
    public static void drawTiledSprite(MatrixStack matrix, int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel, TilingDirection tilingDirection) {
        drawTiledSprite(matrix, xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, textureWidth, textureHeight, zLevel, tilingDirection, true);
    }

    public static void drawTiledSprite(MatrixStack matrix, int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel, TilingDirection tilingDirection, boolean blendAlpha) {
        if (desiredWidth != 0 && desiredHeight != 0 && textureWidth != 0 && textureHeight != 0) {
            Minecraft.getInstance().textureManager.bind(AtlasTexture.LOCATION_BLOCKS);
            int xTileCount = desiredWidth / textureWidth;
            int xRemainder = desiredWidth - xTileCount * textureWidth;
            int yTileCount = desiredHeight / textureHeight;
            int yRemainder = desiredHeight - yTileCount * textureHeight;
            int yStart = yPosition + yOffset;
            float uMin = sprite.getU0();
            float uMax = sprite.getU1();
            float vMin = sprite.getV0();
            float vMax = sprite.getV1();
            float uDif = uMax - uMin;
            float vDif = vMax - vMin;
            if (blendAlpha) {
                RenderSystem.enableBlend();
                RenderSystem.enableAlphaTest();
            }

            BufferBuilder vertexBuffer = Tessellator.getInstance().getBuilder();
            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            Matrix4f matrix4f = matrix.last().pose();

            for (int xTile = 0; xTile <= xTileCount; ++xTile) {
                int width = xTile == xTileCount ? xRemainder : textureWidth;
                if (width == 0) {
                    break;
                }

                int x = xPosition + xTile * textureWidth;
                int maskRight = textureWidth - width;
                int shiftedX = x + textureWidth - maskRight;
                float uLocalDif = uDif * (float) maskRight / (float) textureWidth;
                float uLocalMin;
                float uLocalMax;
                if (tilingDirection.right) {
                    uLocalMin = uMin;
                    uLocalMax = uMax - uLocalDif;
                } else {
                    uLocalMin = uMin + uLocalDif;
                    uLocalMax = uMax;
                }

                for (int yTile = 0; yTile <= yTileCount; ++yTile) {
                    int height = yTile == yTileCount ? yRemainder : textureHeight;
                    if (height == 0) {
                        break;
                    }

                    int y = yStart - (yTile + 1) * textureHeight;
                    int maskTop = textureHeight - height;
                    float vLocalDif = vDif * (float) maskTop / (float) textureHeight;
                    float vLocalMin;
                    float vLocalMax;
                    if (tilingDirection.down) {
                        vLocalMin = vMin;
                        vLocalMax = vMax - vLocalDif;
                    } else {
                        vLocalMin = vMin + vLocalDif;
                        vLocalMax = vMax;
                    }

                    vertexBuffer.vertex(matrix4f, (float) x, (float) (y + textureHeight), (float) zLevel).uv(uLocalMin, vLocalMax).endVertex();
                    vertexBuffer.vertex(matrix4f, (float) shiftedX, (float) (y + textureHeight), (float) zLevel).uv(uLocalMax, vLocalMax).endVertex();
                    vertexBuffer.vertex(matrix4f, (float) shiftedX, (float) (y + maskTop), (float) zLevel).uv(uLocalMax, vLocalMin).endVertex();
                    vertexBuffer.vertex(matrix4f, (float) x, (float) (y + maskTop), (float) zLevel).uv(uLocalMin, vLocalMin).endVertex();
                }
            }

            vertexBuffer.end();
            WorldVertexBufferUploader.end(vertexBuffer);
            if (blendAlpha) {
                RenderSystem.disableAlphaTest();
                RenderSystem.disableBlend();
            }

        }
    }
}
