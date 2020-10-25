package software.bernie.techarium.machine.screen;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.container.AutomaticContainer;

import java.util.ArrayList;
import java.util.List;


public class AutomaticContainerScreen extends ContainerScreen<AutomaticContainer> {

    private final AutomaticContainer container;
    private final ITextComponent title;

    public AutomaticContainerScreen(AutomaticContainer container, PlayerInventory inv, ITextComponent containerName) {
        super(container, inv, containerName);
        this.container = container;
        this.title = containerName;
        xSize = container.getMachineController().getBackgroundSizeXY().getKey();
        ySize = container.getMachineController().getBackgroundSizeXY().getValue();
    }

    @Override
    protected void init() {
        super.init();
        if(!container.getMachineController().getGuiWidgets().isEmpty())
        container.getMachineController().getGuiWidgets().forEach(widget -> this.addButton(widget.create()));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.renderBackground();
        this.container.getMachineController().getBackground().draw(getGuiLeft(), getGuiTop(),getXSize(),getYSize());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int xCenter = (width - xSize) / 2;
        int  yCenter = (height - ySize) / 2;
        this.container.getMachineController().getLazyEnergyStorage().ifPresent(storage -> ((EnergyStorageAddon)storage).renderToolTip(this,guiLeft,guiTop,xCenter,yCenter,mouseX,mouseY));
        this.container.getMachineController().getMultiPogressBar().getProgressBarAddons().forEach(bar -> bar.renderToolTip(this,guiLeft,guiTop,xCenter,yCenter,mouseX,mouseY));
        this.container.getMachineController().getMultiTank().getFluidTanks().forEach(tank -> tank.renderToolTip(this,guiLeft,guiTop,xCenter,yCenter,mouseX,mouseY));
        renderHoveredToolTip(mouseX - xCenter,mouseY- yCenter);
    }

	public List<Rectangle2d> getPanelBounds()
	{
		List<Rectangle2d> panels = new ArrayList<>();
		panels.add(new Rectangle2d(this.guiLeft, this.guiTop, this.xSize, this.ySize));
		container.getMachineController().getGuiWidgets().forEach(widgetFactory ->
		{
			Widget widget = widgetFactory.create();
			panels.add(new Rectangle2d(this.guiLeft + widget.x, this.guiTop + widget.y + 10, widget.getWidth() - 10, widget.getHeight()));
		});
		return panels;
	}
}
