package software.bernie.techarium.machine.addon.inventory;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.machine.interfaces.IWidgetProvider;

import javax.annotation.Nonnull;
import java.util.*;

public class MultiInventoryAddon implements IWidgetProvider, IContainerComponentProvider {

    private final List<InventoryAddon> inventories = new ArrayList<>();
    private LazyOptional<MultiItemCapHandler> invOptional;

    public MultiInventoryAddon() {
        invOptional =  LazyOptional.empty();
    }

    public void add(@Nonnull InventoryAddon addon) {
        this.inventories.add(addon);
        this.rebuildCap();
    }

    public LazyOptional<MultiItemCapHandler> getInvOptional() {
        return invOptional;
    }

    private void rebuildCap(){
        invOptional = LazyOptional.of(()-> {return new MultiItemCapHandler(inventories);} );
    }

    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        List<IFactory<? extends Slot>> components = new ArrayList();
        inventories.forEach(posInv -> {
            components.addAll(posInv.getContainerComponents());
        });
        return components;
    }

    @Override
    public List<IFactory<? extends Widget>> getGuiWidgets() {
        List<IFactory<? extends Widget>> widgets = new ArrayList<>();
        inventories.forEach(posInv -> {
            widgets.addAll(posInv.getGuiWidgets());
        });
        return widgets;
    }


}
