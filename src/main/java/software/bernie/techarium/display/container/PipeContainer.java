package software.bernie.techarium.display.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import software.bernie.techarium.registry.BlockTileRegistry;
import software.bernie.techarium.registry.ContainerRegistry;



public class PipeContainer extends Container {

    public final IWorldPosCallable worldPosCallable;

    public PipeContainer(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(id, playerInventory);
    }

    public PipeContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.NULL);
    }

    public PipeContainer(int id, PlayerInventory playerInventory, final IWorldPosCallable worldCallable) {
        super(ContainerRegistry.PIPE_CONTAINER.get(), id);
        worldPosCallable = worldCallable;
    }
    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(worldPosCallable, playerIn, BlockTileRegistry.PIPE.getBlock());
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
        /*
        ItemStack originalStack = ItemStack.EMPTY;
        Slot fromSlot = this.inventorySlots.get(slot);
        if (fromSlot != null && fromSlot.getHasStack()) {
            ItemStack modifyStack = fromSlot.getStack();
            originalStack = modifyStack.copy();
            if (slot < inventory.getSizeInventory()) {
                if (!this.mergeItemStack(modifyStack, inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(modifyStack, 0, inventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (modifyStack.isEmpty()) {
                fromSlot.putStack(ItemStack.EMPTY);
            } else {
                fromSlot.onSlotChanged();
            }
        }
        return originalStack;*/
    }
}
