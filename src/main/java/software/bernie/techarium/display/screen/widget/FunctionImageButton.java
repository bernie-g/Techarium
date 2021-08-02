package software.bernie.techarium.display.screen.widget;

import lombok.Setter;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.util.Vector2i;


public class FunctionImageButton extends ImageButton {

    @Setter
    private Runnable runnable;

     public FunctionImageButton(Vector2i position, Vector2i size, Vector2i texturePosition, int yTextureOffset, ResourceLocation resourceLocationIn, Runnable func) {
         super(position.getX(), position.getY(), size.getX(), size.getY(), texturePosition.getX(), texturePosition.getY(), yTextureOffset, resourceLocationIn, null);
         this.runnable = func;
     }

    @Override
    public void onPress() {
        runnable.run();
    }
}
