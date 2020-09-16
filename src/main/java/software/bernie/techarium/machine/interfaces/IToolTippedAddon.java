package software.bernie.techarium.machine.interfaces;

import net.minecraft.client.gui.screen.Screen;

public interface IToolTippedAddon {

    default void renderToolTip(Screen screen, int x, int y,int xSize,int ySize,int mouseX, int mouseY) {

    }

}
