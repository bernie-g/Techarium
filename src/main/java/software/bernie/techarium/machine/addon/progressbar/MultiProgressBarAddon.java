package software.bernie.techarium.machine.addon.progressbar;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.inventory.container.Slot;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.machine.interfaces.IWidgetProvider;

import java.util.ArrayList;
import java.util.List;

public class MultiProgressBarAddon implements IWidgetProvider, IContainerComponentProvider {

    private final List<ProgressBarAddon> progressBarAddons = new ArrayList<>();

    public MultiProgressBarAddon() {
    }

    public void add(ProgressBarAddon progressBarAddon){
        this.progressBarAddons.add(progressBarAddon);
    }

    public void attemptTickAllBars() {
        if(!progressBarAddons.isEmpty()) {
            this.progressBarAddons.forEach(progressBarAddon -> {
                        if (progressBarAddon.getCanProgress().test(progressBarAddon.getTile())) {
                            if (progressBarAddon.canProgressUp() && progressBarAddon.getProgress() == 0) {
                                progressBarAddon.getOnProgressStart().run();
                            }

                            if (!progressBarAddon.canProgressUp() && progressBarAddon.getProgress() == progressBarAddon.getMaxProgress()) {
                                progressBarAddon.getOnProgressStart().run();
                            }
                            progressBarAddon.onTick();
                        } else if (progressBarAddon.getCanReset().test(progressBarAddon.getTile())) {
                            progressBarAddon.setProgress(progressBarAddon.canProgressUp() ? 0 : progressBarAddon.getMaxProgress());
                        }
                    }
            );
        }
    }

    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        List<IFactory<? extends Slot>> components = new ArrayList<>();
        progressBarAddons.forEach(posPBar -> {
            components.addAll(posPBar.getContainerComponents());
        });
        return components;
    }

    @Override
    public List<IFactory<? extends Widget>> getGuiWidgets() {
        List<IFactory<? extends Widget>> widgets = new ArrayList<>();
        progressBarAddons.forEach(posPBar -> {
            widgets.addAll(posPBar.getGuiWidgets());
        });
        return widgets;
    }
}
