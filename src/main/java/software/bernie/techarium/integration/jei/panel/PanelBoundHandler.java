package software.bernie.techarium.integration.jei.panel;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rectangle2d;
import software.bernie.techarium.display.screen.AutomaticContainerScreen;

import java.util.List;

public class PanelBoundHandler implements IGuiContainerHandler<AutomaticContainerScreen>
{
	@Override
	public List<Rectangle2d> getGuiExtraAreas(AutomaticContainerScreen containerScreen)
	{
		return containerScreen.getPanelBounds();
	}
}
