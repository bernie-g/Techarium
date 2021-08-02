package software.bernie.techarium.display.screen;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.machine.addon.fluid.MultiFluidTankAddon;
import software.bernie.techarium.machine.addon.inventory.DrawableInventoryAddon;
import software.bernie.techarium.machine.addon.inventory.InventoryAddon;
import software.bernie.techarium.machine.addon.inventory.MultiInventoryAddon;
import software.bernie.techarium.machine.addon.progressbar.MultiProgressBarAddon;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.display.screen.widget.DrawableWidget;
import software.bernie.techarium.display.screen.widget.EnergyAutoWidget;
import software.bernie.techarium.display.screen.widget.ProgressBarWidget;
import software.bernie.techarium.display.screen.widget.TankWidget;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class WidgetProvider {
    public static List<IFactory<DrawableWidget>> getWidgets(MachineController controller) {
        List<IFactory<DrawableWidget>> widgets = new ArrayList<>();
        if (controller.isPowered()) {
            widgets.addAll(getEnergyWidgets(controller.getEnergyStorage()));
        }
        if (controller.getMultiInventory() != null) {
            widgets.addAll(getMultiInventoryWidgets(controller.getMultiInventory()));
        }
        if (controller.getMultiTank() != null) {
            widgets.addAll(getMultiTankWidgets(controller.getMultiTank()));
        }
        if (controller.getMultiProgressBar() != null) {
            widgets.addAll(getMultiProgressBarWidgets(controller.getMultiProgressBar()));
        }
        return widgets;
    }

    private static List<IFactory<DrawableWidget>> getEnergyWidgets(EnergyStorageAddon energyStorageAddon) {
        return Lists.newArrayList(() -> new EnergyAutoWidget(energyStorageAddon));
    }

    private static List<IFactory<DrawableWidget>> getMultiInventoryWidgets(MultiInventoryAddon multiInventoryAddon) {
        List<IFactory<DrawableWidget>> widgets = new ArrayList<>();
        multiInventoryAddon.getInventories().forEach(posInv ->
            widgets.addAll(getInventoryAddonWidgets(posInv))
        );
        return widgets;
    }

    private static List<IFactory<DrawableWidget>> getInventoryAddonWidgets(InventoryAddon inventoryAddon) {
        if (inventoryAddon instanceof DrawableInventoryAddon) {
            DrawableInventoryAddon drawableInventoryAddon = (DrawableInventoryAddon) inventoryAddon;
            return Lists.newArrayList(() -> new DrawableWidget(drawableInventoryAddon.getBackground(), drawableInventoryAddon.getBackgroundXPos(), drawableInventoryAddon.getBackgroundYPos(), drawableInventoryAddon.getBackgroundXSize(), drawableInventoryAddon.getBackgroundYSize(), new StringTextComponent("inventory")));
        }
        return new ArrayList<>();
    }

    private static List<IFactory<DrawableWidget>> getMultiTankWidgets(MultiFluidTankAddon multiFluidTankAddon) {
        List<IFactory<DrawableWidget>> widgets = new ArrayList<>();
        multiFluidTankAddon.getFluidTanks().forEach(fluidTankAddon -> widgets.addAll(getFluidTankWidgets(fluidTankAddon)));
        return widgets;
    }

    private static List<IFactory<DrawableWidget>> getFluidTankWidgets(FluidTankAddon fluidTankAddon) {
        return Lists.newArrayList(() -> new TankWidget(fluidTankAddon, fluidTankAddon.getTankDrawable(), fluidTankAddon.getPosX(), fluidTankAddon.getPosY(), fluidTankAddon.getSize().getX(), fluidTankAddon.getSize().getY(), fluidTankAddon.getName()));
    }

    private static List<IFactory<DrawableWidget>> getMultiProgressBarWidgets(MultiProgressBarAddon multiProgressBarAddon) {
        List<IFactory<DrawableWidget>> widgets = new ArrayList<>();
        multiProgressBarAddon.getProgressBarAddons().forEach(posPBar -> widgets.addAll(getProgressBarWidget(posPBar)));
        return widgets;
    }

    private static List<IFactory<DrawableWidget>> getProgressBarWidget(ProgressBarAddon progressBarAddon) {
        return Lists.newArrayList(() -> new ProgressBarWidget(progressBarAddon, progressBarAddon.getPosX(), progressBarAddon.getPosY(), progressBarAddon.getSize(), new StringTextComponent(progressBarAddon.getName())));
    }
}
