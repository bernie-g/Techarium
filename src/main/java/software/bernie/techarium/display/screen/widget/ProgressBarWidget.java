package software.bernie.techarium.display.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;
import software.bernie.techarium.machine.interfaces.ITooltipAddon;
import software.bernie.techarium.machine.interfaces.ITooltipProvider;
import software.bernie.techarium.util.Vector2i;

public class ProgressBarWidget extends DrawableWidget implements ITooltipProvider {

    private final ProgressBarAddon progressBar;

    public ProgressBarWidget(ProgressBarAddon progressBar, int xIn, int yIn, Vector2i size, ITextComponent msg) {
        super(progressBar.getDrawable(), xIn, yIn, size.getX(), size.getY(), msg);
        this.progressBar = progressBar;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int length = (int)(((float)progressBar.getProgress() / progressBar.getMaxProgress()) * getWidth());
        progressBar.getDrawable().drawPartial(matrixStack, new Vector2i(x, y), progressBar.getDrawable().getSize().copy().setX(length), progressBar.getDrawable().getTexturePos());
    }

    @Override
    public ITooltipAddon getTooltip() {
        return progressBar;
    }
}
