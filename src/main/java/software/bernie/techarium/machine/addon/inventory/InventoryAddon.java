package software.bernie.techarium.machine.addon.inventory;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.machine.container.component.SlotComponent;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.tile.base.MachineMasterTile;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

@Accessors(chain = true)
@Getter
@Setter
public class InventoryAddon extends ItemStackHandler implements IContainerComponentProvider {

    private final String name;
    private int xPos;
    private int yPos;
    private int xSize;
    private int ySize;
    private BiPredicate<ItemStack, Integer> insertPredicate;
    private BiPredicate<ItemStack, Integer> extractPredicate;
    private BiConsumer<ItemStack, Integer> onSlotChanged;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<Integer, Integer> slotStackSizes;

    private Function<Integer, Pair<Integer, Integer>> slotPosition;

    private MachineMasterTile<?> tile;

    private int slotLimit;

    public InventoryAddon(MachineMasterTile<?> tile, String name, int xPos, int yPos, int slots) {
        super(slots);
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.insertPredicate = (stack, integer) -> true;
        this.extractPredicate = (stack, integer) -> true;
        this.onSlotChanged = (stack, integer) -> {
        };
        this.slotStackSizes = new HashMap<>();
        this.slotLimit = 64;
        this.tile = tile;
        setSize(slots);
        setRange(slots, 1);
        setSlotPositionWithOffset(18);
    }

    public InventoryAddon setOnSlotChanged(BiConsumer<ItemStack, Integer> onSlotChanged) {
        this.onSlotChanged = onSlotChanged;
        return this;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        validateSlotIndex(slot);
        ItemStack existingStack = this.stacks.get(slot);
        int limit = getStackLimit(slot, stack);
        if (!existingStack.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existingStack)) {
                return stack;
            }
            limit -= existingStack.getCount();
        }
        if (limit <= 0) {
            return stack;
        }
        boolean reachedLimit = stack.getCount() > limit;
        if (!simulate) {
            if (existingStack.isEmpty()) {
                this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existingStack.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }
        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    protected void onContentsChanged(int slot) {
        //TODO :: Update Tile
        onSlotChanged.accept(getStackInSlot(slot), slot);
    }

    public InventoryAddon setRange(int x, int y) {
        this.xSize = x;
        this.ySize = y;
        return this;
    }

    public InventoryAddon setSlotPositionWithOffset(int dx, int dy) {
        setSlotPosition(integer -> Pair.of(dx * (integer % xSize), dy * (integer / xSize)));
        return this;
    }

    public InventoryAddon setSlotPositionWithOffset(int combined) {
        return setSlotPositionWithOffset(combined, combined);
    }

    public MachineMasterTile<?> getMachineTile() {
        return tile;
    }

    public InventoryAddon setSlotStackSize(int slot, int limit) {
        this.slotStackSizes.put(slot, limit);
        return this;
    }

    public int getSlotStackSize(int slot){
        return slotStackSizes.getOrDefault(slot,this.slotLimit);
    }

    @Override
    public int getSlotLimit(int slot) {
        return getSlotStackSize(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return insertPredicate.test(stack, slot);
    }

    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        List<IFactory<? extends Slot>> slots = Lists.newArrayList();
        for (AtomicInteger x = new AtomicInteger(); x.get() < xSize; x.incrementAndGet()) {
            for (AtomicInteger y = new AtomicInteger(); y.get() < ySize; y.incrementAndGet()) {
                int index = x.get() + y.get()*ySize;
                Pair<Integer, Integer> position = slotPosition.apply(index);
                slots.add(() -> new SlotComponent(this, index, position.getLeft() + getXPos(), position.getRight() + getYPos()));
            }
        }
        return slots;
    }
}
