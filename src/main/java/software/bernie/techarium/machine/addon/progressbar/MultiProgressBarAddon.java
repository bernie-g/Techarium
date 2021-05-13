package software.bernie.techarium.machine.addon.progressbar;

import net.minecraft.inventory.container.Slot;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;

import java.util.ArrayList;
import java.util.List;

public class MultiProgressBarAddon implements IContainerComponentProvider {

    private final List<ProgressBarAddon> progressBarAddons = new ArrayList<>();

    public MultiProgressBarAddon() {
    }

    public void add(ProgressBarAddon progressBarAddon) {
        this.progressBarAddons.add(progressBarAddon);
    }

    public void attemptTickAllBars() {
        if (!progressBarAddons.isEmpty()) {
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

    public List<ProgressBarAddon> getProgressBarAddons() {
        return progressBarAddons;
    }

    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        List<IFactory<? extends Slot>> components = new ArrayList<>();
        progressBarAddons.forEach(posPBar -> {
            components.addAll(posPBar.getContainerComponents());
        });
        return components;
    }
}
