package software.bernie.techarium.machine.screen.widget;

import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.controller.MachineController;

public class DrawableWidget extends Widget {

    private final IDrawable drawable;
    private final MachineController controller;

    public DrawableWidget(MachineController controller, IDrawable drawable, int xIn, int yIn, String msg) {
        super(xIn, yIn, msg);
        this.drawable = drawable;
        this.controller = controller;
    }

    public DrawableWidget(MachineController controller, IDrawable drawable,int xIn, int yIn, int widthIn, int heightIn, String msg) {
        super(xIn, yIn, widthIn, heightIn, msg);
        this.drawable = drawable;
        this.controller = controller;
    }

    private Pair<Integer, Integer> getBackgroundSize() {
        return controller.getBackgroundSizeXY();
    }

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenY = minecraft.getMainWindow().getScaledHeight() / 2;
        int screenX = minecraft.getMainWindow().getScaledWidth() / 2;
        drawable.draw(screenX - getBackgroundSize().getValue() / 2 + x, screenY - getBackgroundSize().getKey() / 2 + y, getWidth(), getHeight());
    }
}
