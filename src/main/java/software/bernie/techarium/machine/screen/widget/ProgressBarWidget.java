package software.bernie.techarium.machine.screen.widget;

import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;

public class ProgressBarWidget extends Widget {

    private final ProgressBarAddon progressBar;

    public ProgressBarWidget(ProgressBarAddon progressBar, int xIn, int yIn, int widthIn, int heightIn, String msg) {
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
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenY = minecraft.getMainWindow().getScaledHeight() / 2;
        int screenX = minecraft.getMainWindow().getScaledWidth() / 2;
        int guiLeft = getBackgroundSize().getKey() / 2;
        int guiTop = getBackgroundSize().getValue() / 2;
        getProgressBar().getDrawable().drawPartial(screenX - guiLeft + x, screenY - guiTop + y, getWidth(), getHeight(), (float)getProgressBar().getProgress() / getProgressBar().getMaxProgress(), 1, 0, 0);
    }
}
