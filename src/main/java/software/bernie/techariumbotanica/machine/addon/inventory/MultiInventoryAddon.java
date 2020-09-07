package software.bernie.techariumbotanica.machine.addon.inventory;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techariumbotanica.machine.interfaces.IContainerComponentProvider;
import software.bernie.techariumbotanica.machine.interfaces.IFactory;
import software.bernie.techariumbotanica.machine.interfaces.IWidgetProvider;
import software.bernie.techariumbotanica.machine.sideness.Side;

import javax.annotation.Nonnull;
import java.util.*;

public class MultiInventoryAddon implements IWidgetProvider, IContainerComponentProvider {

    private final List<InventoryAddon> inventories = new ArrayList<>();
    private final Map<Side, LazyOptional<MultiItemCapHandler>> lazyOptionals = new HashMap<>();

    public MultiInventoryAddon() {
        this.lazyOptionals.put(null, LazyOptional.empty());
        Side[] sides = Side.values();
        int length = sides.length;

        for (int x = 0; x < length; ++x) {
            Side side = sides[x];
            this.lazyOptionals.put(side, LazyOptional.empty());
        }
    }

    public void add(@Nonnull InventoryAddon addon) {
        this.inventories.add(addon);
        this.buildCap(new Side[]{null});
        this.buildCap(Side.values());
    }

    private void buildCap(Side[] sides) {
        int length = sides.length;
        for (int x = 0; x < length; ++x) {
            Side side = sides[x];
            this.lazyOptionals.get(side).invalidate();
            this.lazyOptionals.put(side,LazyOptional.of(() ->{
                return new MultiItemCapHandler(this.getSideHandlers(side));
            }));
        }
    }

    private List<InventoryAddon> getSideHandlers(Side side){
        if(side == null){
            return new ArrayList(this.inventories);
        }else{
            List<InventoryAddon> invList = new ArrayList<>();
            for (InventoryAddon inventory : this.inventories) {
                boolean todo = false;
                if (todo) {
                    //TODO Add sided handlers for handling In and Out of external Pipes.
                } else {
                    inventories.add(inventory);
                }
            }
            return invList;
        }
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
