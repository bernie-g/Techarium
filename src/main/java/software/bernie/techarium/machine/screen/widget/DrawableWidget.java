package software.bernie.techarium.machine.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.controller.MachineController;

public class DrawableWidget extends Widget {

    private final IDrawable drawable;
    private final MachineController<?> controller;


    public DrawableWidget(MachineController<?> controller, IDrawable drawable,int xIn, int yIn, int widthIn, int heightIn, ITextComponent msg) {
        super(xIn, yIn, widthIn, heightIn, msg);
        this.drawable = drawable;
        this.controller = controller;
    }

    private Pair<Integer, Integer> getBackgroundSize() {
        return controller.getBackgroundSizeXY();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenY = minecraft.getWindow().getGuiScaledHeight() / 2;
        int screenX = minecraft.getWindow().getGuiScaledWidth() / 2;
        drawable.draw(screenX - getBackgroundSize().getValue() / 2 + x, screenY - getBackgroundSize().getKey() / 2 + y, getWidth(), getHeight());
    }
}
