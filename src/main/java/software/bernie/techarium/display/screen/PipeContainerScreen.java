package software.bernie.techarium.display.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.client.screen.draw.UiTexture;
import software.bernie.techarium.display.container.PipeContainer;

import static software.bernie.techarium.Techarium.ModID;

public class PipeContainerScreen extends DrawableContainerScreen<PipeContainer> {

    private static final IDrawable BACKGROUND_TEXTURE = new UiTexture(new ResourceLocation(ModID, "textures/gui/pipe/inventory.png"), 201, 195).getFullArea();

    private boolean isInput = true;

    public PipeContainerScreen(PipeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        imageWidth = 201;
        imageHeight = 195;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
    }

    @Override
    protected IDrawable getBackground() {
        return BACKGROUND_TEXTURE;
    }
}
