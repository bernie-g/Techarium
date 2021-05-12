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
        xSize = container.getMachineController().getBackgroundSizeXY().getKey();
        ySize = container.getMachineController().getBackgroundSizeXY().getValue();
    }

    @Override
    protected void init() {
        super.init();
        List<IFactory<? extends Widget>> widgets = WidgetProvider.getWidgets(container.getMachineController());
        if (!widgets.isEmpty())
            widgets.forEach(widget -> this.addButton(widget.create()));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        this.container.getMachineController().getBackground().draw(getGuiLeft(), getGuiTop(), getXSize(), getYSize());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        int xCenter = (width - xSize) / 2;
        int yCenter = (height - ySize) / 2;
        this.container.getMachineController().getLazyEnergyStorage().ifPresent(storage -> ((EnergyStorageAddon) storage).renderToolTip(this, guiLeft, guiTop, xCenter, yCenter, mouseX, mouseY));
        this.container.getMachineController().getMultiProgressBar().getProgressBarAddons().forEach(bar -> bar.renderToolTip(this, guiLeft, guiTop, xCenter, yCenter, mouseX, mouseY));
        this.container.getMachineController().getMultiTank().getFluidTanks().forEach(tank -> tank.renderToolTip(this, guiLeft, guiTop, xCenter, yCenter, mouseX, mouseY));
        renderHoveredTooltip(matrixStack, mouseX - xCenter, mouseY - yCenter);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = 0; i < container.getMachineController().getMultiTank().getFluidTanks().size(); i++) {
            FluidTankAddon fluidTankAddon = container.getMachineController().getMultiTank().getFluidTanks().get(i);
            if (isPointInRegion(fluidTankAddon.getPosX(), fluidTankAddon.getPosY(), fluidTankAddon.getSizeX(), fluidTankAddon.getSizeY(), mouseX, mouseY)) {
                NetworkConnection.INSTANCE.sendToServer(new FluidTankClickContainerPacket(this.getContainer(), button, hasShiftDown(), i));
                return true;
            }
        }
        EnergyStorageAddon energy = container.getMachineController().getEnergyStorage();
        if (isPointInRegion(energy.getPosX(), energy.getPosY(), energy.getSizeX(), energy.getSizeY(), mouseX, mouseY)) {
            NetworkConnection.INSTANCE.sendToServer(new EnergyBarClickContainerPacket(this.getContainer(), button));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public List<Rectangle2d> getPanelBounds() {
        List<Rectangle2d> panels = new ArrayList<>();
        panels.add(new Rectangle2d(this.guiLeft, this.guiTop, this.xSize, this.ySize));
        WidgetProvider.getWidgets(container.getMachineController()).forEach(widgetFactory ->
        {
            Widget widget = widgetFactory.create();
            panels.add(new Rectangle2d(this.guiLeft + widget.x, this.guiTop + widget.y + 10, widget.getWidth(), widget.getHeightRealms()));
        });
        return panels;
    }
}
