package software.bernie.techarium.machine.addon.inventory;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.machine.interfaces.IWidgetProvider;
import software.bernie.techarium.machine.sideness.Side;

import javax.annotation.Nonnull;
import java.util.*;

public class MultiInventoryAddon implements IWidgetProvider, IContainerComponentProvider {

    private final List<InventoryAddon> inventories = new ArrayList<>();
    private final List<LazyOptional<MultiItemCapHandler>> lazyOptionals =  new ArrayList<>();

    public MultiInventoryAddon() {
        this.lazyOptionals.add(LazyOptional.empty());
    }

    public void add(@Nonnull InventoryAddon addon) {
        this.inventories.add(addon);

    }

    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        return null;
    }

    @Override
    public List<IFactory<? extends Widget>> getGuiWidgets() {
        return null;
    }


}
