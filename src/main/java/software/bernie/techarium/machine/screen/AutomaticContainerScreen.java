package software.bernie.techarium.machine.screen;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.container.AutomaticContainer;


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
        this.container.getMachineController().getLazyEnergyStorage().ifPresent(storage -> ((EnergyStorageAddon)storage).renderToolTip(this,guiLeft,guiTop,mouseX,mouseY));
        this.container.getMachineController().getMultiPogressBar().getProgressBarAddons().forEach(bar -> bar.renderToolTip(this,guiLeft,guiTop,mouseX,mouseY));
        this.container.getMachineController().getMultiTank().getFluidTanks().forEach(tank -> tank.renderToolTip(this,guiLeft,guiTop,mouseX,mouseY));
    }
}
