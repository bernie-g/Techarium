package software.bernie.techarium.display.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.network.container.EnergyBarClickContainerPacket;
import software.bernie.techarium.network.container.FluidTankClickContainerPacket;
import software.bernie.techarium.network.NetworkConnection;

import java.util.ArrayList;
import java.util.List;


public class AutomaticContainerScreen extends DrawableContainerScreen<AutomaticContainer> {

    private final ITextComponent title;

    public AutomaticContainerScreen(AutomaticContainer container, PlayerInventory inv, ITextComponent containerName) {
        super(container, inv, containerName);
        this.title = containerName;
        imageWidth = container.getMachineController().getBackgroundSizeXY().getKey();
        imageHeight = container.getMachineController().getBackgroundSizeXY().getValue();
    }

    @Override
    protected void init() {
        super.init();
        List<IFactory<? extends Widget>> widgets = WidgetProvider.getWidgets(getMenu().getMachineController());
        if (!widgets.isEmpty())
            widgets.forEach(widget -> this.addButton(widget.create()));
    }

    @Override
    protected IDrawable getBackground() {
        return getMenu().getMachineController().getBackground();
    }

    @Override
    protected void renderCustomToolTips(MatrixStack matrixStack, int mouseX, int mouseY, int xCenter, int yCenter) {
        super.renderCustomToolTips(matrixStack, mouseX, mouseY, xCenter, yCenter);
        getMenu().getMachineController().getLazyEnergyStorage().ifPresent(storage -> ((EnergyStorageAddon) storage).renderToolTip(this, leftPos, topPos, xCenter, yCenter, mouseX, mouseY));
        getMenu().getMachineController().getMultiProgressBar().getProgressBarAddons().forEach(bar -> bar.renderToolTip(this, leftPos, topPos, xCenter, yCenter, mouseX, mouseY));
        getMenu().getMachineController().getMultiTank().getFluidTanks().forEach(tank -> tank.renderToolTip(this, leftPos, topPos, xCenter, yCenter, mouseX, mouseY));
    }



    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = 0; i < getMenu().getMachineController().getMultiTank().getFluidTanks().size(); i++) {
            FluidTankAddon fluidTankAddon = getMenu().getMachineController().getMultiTank().getFluidTanks().get(i);
            if (isHovering(fluidTankAddon.getPosX(), fluidTankAddon.getPosY(), fluidTankAddon.getSizeX(), fluidTankAddon.getSizeY(), mouseX, mouseY)) {
                NetworkConnection.INSTANCE.sendToServer(new FluidTankClickContainerPacket(this.getMenu(), button, hasShiftDown(), i));
                return true;
            }
        }
        EnergyStorageAddon energy = getMenu().getMachineController().getEnergyStorage();
        if (isHovering(energy.getPosX(), energy.getPosY(), energy.getSizeX(), energy.getSizeY(), mouseX, mouseY)) {
            NetworkConnection.INSTANCE.sendToServer(new EnergyBarClickContainerPacket(this.getMenu(), button));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public List<Rectangle2d> getPanelBounds() {
        List<Rectangle2d> panels = new ArrayList<>();
        panels.add(new Rectangle2d(this.leftPos, this.topPos, this.imageWidth, this.imageHeight));
        WidgetProvider.getWidgets(getMenu().getMachineController()).forEach(widgetFactory ->
        {
            Widget widget = widgetFactory.create();
            panels.add(new Rectangle2d(this.leftPos + widget.x, this.topPos + widget.y + 10, widget.getWidth(), widget.getHeight()));
        });
        return panels;
    }
}
