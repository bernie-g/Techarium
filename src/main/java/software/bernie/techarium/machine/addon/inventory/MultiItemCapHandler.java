package software.bernie.techarium.machine.addon.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class MultiItemCapHandler extends ItemStackHandler {

    private final List<InventoryAddon> inventories;
    private int slotAmount;

    public MultiItemCapHandler(List<InventoryAddon> inventories) {
        this.inventories = inventories;
        for (InventoryAddon inventory : this.inventories) {
            slotAmount += inventory.getSlots();
        }
    }

    @Override
    public int getSlots() {
        return slotAmount;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        InventoryAddon inventory = getInvFromSlot(slot);
        if (inventory != null) {
            if (inventory.getInsertPredicate().test(stack, slot)) {
                return inventory.insertItem(getRelativeSlot(inventory, slot), stack, simulate);
            } else {
                return stack;
            }
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        InventoryAddon inventory = getInvFromSlot(slot);
        if (inventory != null) {
            int rSlot = getRelativeSlot(inventory, slot);
            if (!inventory.getExtractPredicate().test(inventory.getStackInSlot(rSlot), rSlot)) {
                return ItemStack.EMPTY;
            } else {
                return inventory.extractItem(rSlot, amount, simulate);
            }
        }
        return super.extractItem(slot, amount, simulate);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        InventoryAddon inventory = getInvFromSlot(slot);
        if (inventory != null) {
            return inventory.getStackInSlot(getRelativeSlot(inventory, slot));
        }

        return super.getStackInSlot(slot);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        InventoryAddon inventory = getInvFromSlot(slot);
        if (inventory != null) {
            inventory.setStackInSlot(getRelativeSlot(inventory, slot), stack);
        }
        super.setStackInSlot(slot, stack);
    }

    public List<InventoryAddon> getInventories() {
        return inventories;
    }

    public InventoryAddon getInvFromSlot(int slot) {
        for (InventoryAddon inventory : this.inventories) {
            slot -= inventory.getSlots();
            if (slot < 0) {
                return inventory;
            }
        }
        return null;
    }

    public int getRelativeSlot(InventoryAddon inventory, int slot) {
        for (InventoryAddon inv : this.inventories) {
            if (inv.equals(inventory)) return slot;
            slot -= inv.getSlots();
        }
        return 0;
    }

}
