package software.bernie.techarium.machine.addon.inventory;

import com.google.common.collect.Lists;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.tile.base.MachineTile;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class InventoryAddon extends ItemStackHandler implements IContainerComponentProvider {

    private final String name;
    private int xPos;
    private int yPos;
    private int xSize;
    private int ySize;
    private BiPredicate<ItemStack, Integer> insertPredicate;
    private BiPredicate<ItemStack, Integer> extractPredicate;
    private BiConsumer<ItemStack, Integer> onSlotChanged;

    private Map<Integer, Integer> slotAmountFilter;
    private Map<Integer, ItemStack> slotToStackRenderMap;

    private Function<Integer, Pair<Integer, Integer>> slotPosition;

    private MachineTile tile;

    private int slotLimit;

    public InventoryAddon(MachineTile tile, String name, int xPos, int yPos, int slots){
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.insertPredicate = (stack,integer) -> true;
        this.extractPredicate = (stack,integer) -> true;
        this.onSlotChanged = (stack,integer) -> {
        };
        this.slotAmountFilter = new HashMap<>();
        this.slotToStackRenderMap = new HashMap<>();
        this.slotLimit = 64;
        this.tile = tile;
        setSize(slots);
        setRange(slots,1);
        this.slotPosition = integer -> Pair.of(18 * (integer % xSize), 18 * (integer / xSize));
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

    public MachineTile getMachineTile() {
        return tile;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public String getName() {
        return name;
    }

    public InventoryAddon setInputFilter(BiPredicate<ItemStack, Integer> predicate) {
        this.insertPredicate = predicate;
        return this;
    }

    public BiPredicate<ItemStack, Integer> getInsertPredicate() {
        return insertPredicate;
    }

    public InventoryAddon setExtractFilter(BiPredicate<ItemStack, Integer> predicate) {
        this.extractPredicate = predicate;
        return this;
    }

    public BiPredicate<ItemStack, Integer> getExtractPredicate() {
        return extractPredicate;
    }

    public InventoryAddon setSlotAmount(int amount){
        slotLimit = amount;
        return this;
    }

    public InventoryAddon setSlotLimit(int slot, int limit) {
        this.slotAmountFilter.put(slot, limit);
        return this;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return insertPredicate.test(stack, slot);
    }

    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        return Lists.newArrayList(() -> {
            return new SlotItemHandler(this,0,getXPos(),getYPos());
        });
    }
}
