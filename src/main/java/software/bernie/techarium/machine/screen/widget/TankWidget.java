package software.bernie.techarium.machine.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.client.screen.draw.UiTexture;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.util.GuiUtils;
import software.bernie.techarium.util.TilingDirection;

import java.awt.*;

public class TankWidget extends Widget {

    private final IDrawable drawable;

    private final FluidTankAddon tank;

    public TankWidget(FluidTankAddon tank, IDrawable drawable, int xIn, int yIn, int widthIn, int heightIn,
                      String msg) {
        super(xIn, yIn, widthIn, heightIn, ITextComponent.nullToEmpty(msg));
        this.drawable = drawable;
        this.tank = tank;
    }

    private Pair<Integer, Integer> getBackgroundSize() {
        return tank.getBackgroundSizeXY();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenY = minecraft.getWindow().getGuiScaledHeight() / 2;
        int screenX = minecraft.getWindow().getGuiScaledWidth() / 2;
        int guiLeft = getBackgroundSize().getKey() / 2;
        int guiTop = getBackgroundSize().getValue() / 2;

        int x = screenX - guiLeft + this.x;
        int y = screenY - guiTop + this.y;

        if (!this.tank.getFluid().isEmpty()) {
            FluidStack stack = this.tank.getFluid();
            int stored = this.tank.getFluidAmount();
            int capacity = this.tank.getCapacity();

            float start = 1 - (float) stored / capacity;
            float offset = ((getHeight() - tank.getBottomOffset() - tank.getTopOffset()) * start);
            int height = stored * (getHeight() - tank.getBottomOffset() - tank.getTopOffset()) / capacity;

            ResourceLocation flowing = stack.getFluid().getAttributes().getStillTexture(stack);
            if (flowing != null) {
                Texture texture = minecraft.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS);
                if (texture instanceof AtlasTexture) {
                    TextureAtlasSprite sprite = ((AtlasTexture) texture).getSprite(flowing);
                    minecraft.getTextureManager().bind(AtlasTexture.LOCATION_BLOCKS);
                    Color color = new Color(stack.getFluid().getAttributes().getColor());
                    RenderSystem.color4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F,
                            (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
                    RenderSystem.enableBlend();
                    matrixStack.pushPose();
                    int yPosition = (int) (y + tank.getTopOffset() + offset) + height;
                    GuiUtils.drawTiledSprite(matrixStack, x + tank.getLeftOffset(),
                            yPosition, 1,
                            getWidth() - tank.getLeftOffset() - tank.getRightOffset(), height, sprite, 16, 16, 0,
                            TilingDirection.DOWN_RIGHT);
/*                    GuiUtils.drawTiledSprite(matrixStack, x + tank.getLeftOffset(),
                            (int) (y + tank.getTopOffset()), 1,
                            getWidth() - tank.getLeftOffset() - tank.getRightOffset(), height, sprite, 16, 16, 10000,
                            TilingDirection.DOWN_RIGHT);*/
                    matrixStack.popPose();
                    /*BlitUtil.blit(x + tank.getLeftOffset(), (int) (y + tank.getTopOffset() + offset), 0,
                            getWidth() - tank.getLeftOffset() - tank.getRightOffset(), height, sprite.getMinU(),
                            sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());*/
                    /*BlitUtil.blit(x + tank.getLeftOffset(), (int) (y + tank.getTopOffset() + offset),
                                  tank.getLeftOffset(), tank.getTopOffset(), getWidth(), height, 1, 1);*/
                    RenderSystem.disableBlend();
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
        drawable.draw(x, y, getWidth(), getHeight());
    }

    private IDrawable getFluidTexture(ResourceLocation location) {
        return new UiTexture(location, 16, 16).getFullArea();
    }

}
