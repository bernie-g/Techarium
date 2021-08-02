package software.bernie.techarium.display.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.machine.interfaces.ITooltipAddon;
import software.bernie.techarium.machine.interfaces.ITooltipProvider;
import software.bernie.techarium.util.GuiUtils;
import software.bernie.techarium.util.TilingDirection;

import java.awt.*;

public class TankWidget extends DrawableWidget implements ITooltipProvider {

    private final FluidTankAddon tank;

    public TankWidget(FluidTankAddon tank, IDrawable drawable, int xIn, int yIn, int widthIn, int heightIn,
                      String msg) {
        super(drawable,xIn, yIn, widthIn, heightIn, ITextComponent.nullToEmpty(msg));
        this.tank = tank;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();

        int x = getDrawOffset().getX() + this.x;
        int y = getDrawOffset().getY() + this.y;

        if (!this.tank.getFluid().isEmpty()) {
            FluidStack stack = this.tank.getFluid();
            int stored = this.tank.getFluidAmount();
            int capacity = this.tank.getCapacity();

            float start = 1 - (float) stored / capacity;
            float offset = ((getHeight() - tank.getBottomOffset() - tank.getTopOffset()) * start);
            int height = stored * (getHeight() - tank.getBottomOffset() - tank.getTopOffset()) / capacity;

            ResourceLocation still = stack.getFluid().getAttributes().getStillTexture(stack);
            if (still != null) {
                Texture texture = minecraft.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS);
                if (texture instanceof AtlasTexture) {
                    TextureAtlasSprite sprite = ((AtlasTexture) texture).getSprite(still);
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
                    matrixStack.popPose();
                    RenderSystem.disableBlend();
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public ITooltipAddon getTooltip() {
        return tank;
    }
}
