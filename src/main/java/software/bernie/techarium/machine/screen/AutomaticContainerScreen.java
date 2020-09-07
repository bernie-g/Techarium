package software.bernie.techariumbotanica.machine.screen;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techariumbotanica.machine.container.AutomaticContainer;


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

}
