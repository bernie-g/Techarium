package software.bernie.techariumbotanica.machine.interfaces;

import net.minecraft.client.gui.widget.Widget;
import software.bernie.techariumbotanica.machine.interfaces.IFactory;

import java.util.List;

public interface IWidgetProvider {

    List<IFactory<? extends Widget>> getGuiWidgets();

}
