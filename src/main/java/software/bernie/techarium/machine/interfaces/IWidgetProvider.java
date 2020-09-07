package software.bernie.techarium.machine.interfaces;

import net.minecraft.client.gui.widget.Widget;

import java.util.List;

public interface IWidgetProvider {

    List<IFactory<? extends Widget>> getGuiWidgets();

}
