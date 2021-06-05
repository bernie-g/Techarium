package software.bernie.techarium.machine.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.machine.container.AutomaticContainer;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.network.EnergyBarClickContainerPacket;
import software.bernie.techarium.network.FluidTankClickContainerPacket;
import software.bernie.techarium.network.NetworkConnection;

import java.util.ArrayList;
import java.util.List;


public class AutomaticContainerScreen extends ContainerScreen<AutomaticContainer> {

    private final AutomaticContainer container;
    private final ITextComponent title;

    public AutomaticContainerScreen(AutomaticContainer container, PlayerInventory inv, ITextComponent containerName) {
        super(container, inv, containerName);
        this.container = container;
        this.title = containerName;
        imageWidth = container.getMachineController().getBackgroundSizeXY().getKey();
        imageHeight = container.getMachineController().getBackgroundSizeXY().getValue();
    }

    @Override
    protected void init() {
        super.init();
        List<IFactory<? extends Widget>> widgets = WidgetProvider.getWidgets(container.getMachineController());
        if (!widgets.isEmpty())
            widgets.forEach(widget -> this.addButton(widget.create()));
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        this.container.getMachineController().getBackground().draw(getGuiLeft(), getGuiTop(), getXSize(), getYSize());
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        int xCenter = (width - imageWidth) / 2;
        int yCenter = (height - imageHeight) / 2;
        this.container.getMachineController().getLazyEnergyStorage().ifPresent(storage -> ((EnergyStorageAddon) storage).renderToolTip(this, leftPos, topPos, xCenter, yCenter, mouseX, mouseY));
        this.container.getMachineController().getMultiProgressBar().getProgressBarAddons().forEach(bar -> bar.renderToolTip(this, leftPos, topPos, xCenter, yCenter, mouseX, mouseY));
        this.container.getMachineController().getMultiTank().getFluidTanks().forEach(tank -> tank.renderToolTip(this, leftPos, topPos, xCenter, yCenter, mouseX, mouseY));
        renderTooltip(matrixStack, mouseX - xCenter, mouseY - yCenter);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = 0; i < container.getMachineController().getMultiTank().getFluidTanks().size(); i++) {
            FluidTankAddon fluidTankAddon = container.getMachineController().getMultiTank().getFluidTanks().get(i);
            if (isHovering(fluidTankAddon.getPosX(), fluidTankAddon.getPosY(), fluidTankAddon.getSizeX(), fluidTankAddon.getSizeY(), mouseX, mouseY)) {
                NetworkConnection.INSTANCE.sendToServer(new FluidTankClickContainerPacket(this.getMenu(), button, hasShiftDown(), i));
                return true;
            }
        }
        EnergyStorageAddon energy = container.getMachineController().getEnergyStorage();
        if (isHovering(energy.getPosX(), energy.getPosY(), energy.getSizeX(), energy.getSizeY(), mouseX, mouseY)) {
            NetworkConnection.INSTANCE.sendToServer(new EnergyBarClickContainerPacket(this.getMenu(), button));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public List<Rectangle2d> getPanelBounds() {
        List<Rectangle2d> panels = new ArrayList<>();
        panels.add(new Rectangle2d(this.leftPos, this.topPos, this.imageWidth, this.imageHeight));
        WidgetProvider.getWidgets(container.getMachineController()).forEach(widgetFactory ->
        {
            Widget widget = widgetFactory.create();
            panels.add(new Rectangle2d(this.leftPos + widget.x, this.topPos + widget.y + 10, widget.getWidth(), widget.getHeight()));
        });
        return panels;
    }
}
