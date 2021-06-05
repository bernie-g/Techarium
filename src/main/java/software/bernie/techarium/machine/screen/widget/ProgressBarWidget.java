package software.bernie.techarium.machine.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;

public class ProgressBarWidget extends Widget {

    private final ProgressBarAddon progressBar;

    public ProgressBarWidget(ProgressBarAddon progressBar, int xIn, int yIn, int widthIn, int heightIn, ITextComponent msg) {
        super(xIn, yIn, widthIn, heightIn, msg);
        this.progressBar = progressBar;
    }

    public ProgressBarAddon getProgressBar() {
        return progressBar;
    }

    private Pair<Integer, Integer> getBackgroundSize() {
        return progressBar.getBackgroundSizeXY();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenY = minecraft.getWindow().getGuiScaledHeight() / 2;
        int screenX = minecraft.getWindow().getGuiScaledWidth() / 2;
        int guiLeft = getBackgroundSize().getKey() / 2;
        int guiTop = getBackgroundSize().getValue() / 2;
        getProgressBar().getDrawable().drawPartial(screenX - guiLeft + x, screenY - guiTop + y, getWidth(), getHeight(), (float)getProgressBar().getProgress() / getProgressBar().getMaxProgress(), 1, 0, 0);
    }
}
