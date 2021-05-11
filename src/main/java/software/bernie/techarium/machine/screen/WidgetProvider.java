package software.bernie.techarium.machine.screen;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.widget.Widget;
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
import software.bernie.techarium.machine.screen.widget.DrawableWidget;
import software.bernie.techarium.machine.screen.widget.EnergyAutoWidget;
import software.bernie.techarium.machine.screen.widget.ProgressBarWidget;
import software.bernie.techarium.machine.screen.widget.TankWidget;

import java.util.ArrayList;
import java.util.List;

public class WidgetProvider {
    public static List<IFactory<? extends Widget>> getWidgets(MachineController controller) {
        List<IFactory<? extends Widget>> widgets = new ArrayList<>();
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

    private static List<IFactory<? extends Widget>> getEnergyWidgets(EnergyStorageAddon energyStorageAddon) {
        return Lists.newArrayList(() -> new EnergyAutoWidget(energyStorageAddon));
    }

    private static List<IFactory<? extends Widget>> getMultiInventoryWidgets(MultiInventoryAddon multiInventoryAddon) {
        List<IFactory<? extends Widget>> widgets = new ArrayList<>();
        multiInventoryAddon.getInventories().forEach(posInv -> {
            widgets.addAll(getInventoryAddonWidgets(posInv));
        });
        return widgets;
    }

    private static List<IFactory<? extends Widget>> getInventoryAddonWidgets(InventoryAddon inventoryAddon) {
        if (inventoryAddon instanceof DrawableInventoryAddon) {
            DrawableInventoryAddon drawableInventoryAddon = (DrawableInventoryAddon) inventoryAddon;
            return Lists.newArrayList(() -> new DrawableWidget(drawableInventoryAddon.getMachineTile().getController(), drawableInventoryAddon.getBackground(), drawableInventoryAddon.getBackgroundXPos(), drawableInventoryAddon.getBackgroundYPos(), drawableInventoryAddon.getBackgroundXSize(), drawableInventoryAddon.getBackgroundYSize(), new StringTextComponent("inventory")));
        }
        return new ArrayList<>();
    }

    private static List<IFactory<? extends Widget>> getMultiTankWidgets(MultiFluidTankAddon multiFluidTankAddon) {
        List<IFactory<? extends Widget>> widgets = new ArrayList<>();
        multiFluidTankAddon.getFluidTanks().forEach(fluidTankAddon -> widgets.addAll(getFluidTankWidgets(fluidTankAddon)));
        return widgets;
    }

    private static List<IFactory<? extends Widget>> getFluidTankWidgets(FluidTankAddon fluidTankAddon) {
        return Lists.newArrayList(() -> new TankWidget(fluidTankAddon, fluidTankAddon.getTankDrawable(), fluidTankAddon.getPosX(), fluidTankAddon.getPosY(), fluidTankAddon.getSizeX(), fluidTankAddon.getSizeY(), fluidTankAddon.getName()));
    }

    private static List<IFactory<? extends Widget>> getMultiProgressBarWidgets(MultiProgressBarAddon multiProgressBarAddon) {
        List<IFactory<? extends Widget>> widgets = new ArrayList<>();
        multiProgressBarAddon.getProgressBarAddons().forEach(posPBar -> widgets.addAll(getProgressBarWidget(posPBar)));
        return widgets;
    }

    private static List<IFactory<? extends Widget>> getProgressBarWidget(ProgressBarAddon progressBarAddon) {
        return Lists.newArrayList(() -> new ProgressBarWidget(progressBarAddon, progressBarAddon.getPosX(), progressBarAddon.getPosY(), progressBarAddon.getSizeX(), progressBarAddon.getSizeY(), new StringTextComponent(progressBarAddon.getName())));
    }
}
