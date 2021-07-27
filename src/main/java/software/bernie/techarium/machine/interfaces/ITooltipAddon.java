package software.bernie.techarium.machine.interfaces;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.util.Vector2i;
import java.util.List;

public interface ITooltipAddon extends ITooltipProvider {

    default void renderToolTip(Screen screen, int offsetX, int offsetY, int xSize, int ySize, int mouseX, int mouseY) {
        if (isHovering(offsetX, offsetY, mouseX, mouseY)) {
            screen.renderComponentTooltip(new MatrixStack(), createToolTipMessage(), mouseX - xSize, mouseY - ySize);
        }
    }

    default boolean isHovering(int offsetX, int offsetY, int mouseX, int mouseY) {
        return mouseX >= offsetX + getPosX() && mouseX <= offsetX + getPosX() + getSize().getX()
                && mouseY >= offsetY + getPosY() && mouseY <= offsetY + getPosY() + getSize().getY();
    }
    List<ITextComponent> createToolTipMessage();
    int getPosX();
    int getPosY();
    Vector2i getSize();

    @Override
    default ITooltipAddon getTooltip() {
        return this;
    }
}
