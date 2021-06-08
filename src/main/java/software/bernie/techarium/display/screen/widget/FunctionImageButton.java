package software.bernie.techarium.display.screen.widget;

import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.display.screen.widget.awt.*;


public class FunctionImageButton extends ImageButton {

    private final Action action;

     public FunctionImageButton(Point position, Dimension size, Point texturePosition, int yTextureOffset, ResourceLocation resourceLocationIn, Action func) {
         super(position.x, position.y, size.width, size.height, texturePosition.x, texturePosition.y, yTextureOffset, resourceLocationIn, null);
         action = func;
    }

    @Override
    public void onPress() {
        action.doStuff();
    }
    public interface Action {
        void doStuff();
    }
}
