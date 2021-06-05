package software.bernie.techarium.client.screen.draw;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UiTexture {
    private final ResourceLocation TEXTURE;
    private final float x;
    private final float y;

    public UiTexture(ResourceLocation texture, float x, float y) {
        TEXTURE = texture;
        this.x = x;
        this.y = y;
    }

    public void bindTexture() {
        Minecraft.getInstance().textureManager.bind(TEXTURE);
    }

    public IDrawable getFullArea() {
        return new Area(0, 0, 1, 1, x, y);
    }

    public IDrawable getArea(int x, int y, int w, int h) {
        return new Area((float)x / this.x, (float)y / this.y, (float)w / this.x, h / this.y, w, h);
    }

    private class Area implements IDrawable {

        private final float v, u;
        private final float dv, du;
        private final float width, height;

        public Area(float u, float v, float du, float dv, float width, float height) {
            this.v = v;
            this.u = u;
            this.du = du;
            this.dv = dv;
            this.width = width;
            this.height = height;
        }

        @Override
        public void drawPartial(double x, double y, double width, double height, float x1, float y1, float x2,
                                float y2) {
            bindTexture();

            x1 = (float)(Math.round(x1*width) / width);
            x2 = (float)(Math.round(x2*width) / width);
            y1 = (float)(Math.round(y1*height) / height);
            y2 = (float)(Math.round(y2*height) / height);



            float xi = (float) (x + width * x1);
            float xf = (float) (x + width * x2);
            float yi = (float) (y + height * y1);
            float yf = (float) (y + height * y2);

            float ui = (u + du * x1);
            float uf = (u + du * x2);
            float vi = (v + dv * y1);
            float vf = (v + dv * y2);

            //ui = (float) Math.round(ui * this.width) / this.width;
            //uf = (float) Math.round(uf * this.width) / this.width;
            //vi = (float) Math.floor(vi * this.height) / this.height;
            //vf = (float) Math.floor(vf * this.height)   / this.height;
            Tessellator tesselator = Tessellator.getInstance();
            BufferBuilder buf = tesselator.getBuilder();
            RenderSystem.enableAlphaTest();

            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buf.vertex(xi, yi, 0D).uv(ui, vi).endVertex();
            buf.vertex(xi, yf, 0D).uv(ui, vf).endVertex();
            buf.vertex(xf, yf, 0D).uv(uf, vf).endVertex();
            buf.vertex(xf, yi, 0D).uv(uf, vi).endVertex();
            buf.end();
            WorldVertexBufferUploader.end(buf);
        }

    }
}