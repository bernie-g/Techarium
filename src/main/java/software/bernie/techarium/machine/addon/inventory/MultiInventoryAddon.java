package software.bernie.techarium.machine.addon.inventory;

import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;
import javax.annotation.Nonnull;
import java.util.*;

public class MultiInventoryAddon implements IContainerComponentProvider {


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
        List<IFactory<? extends Slot>> components = new ArrayList<>();
        inventories.forEach(posInv -> {
            components.addAll(posInv.getContainerComponents());
        });
        return components;
    }

    public List<InventoryAddon> getInventories() {
        return inventories;
    }

    public Optional<InventoryAddon> getInventoryByName(String name)
    {
        return inventories.stream().filter(inventoryAddon -> inventoryAddon.getName().equals(name)).findFirst();
    }
}
