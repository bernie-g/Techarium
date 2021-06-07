package software.bernie.techarium.display.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import software.bernie.techarium.pipe.PipePosition;
import software.bernie.techarium.registry.BlockTileRegistry;
import software.bernie.techarium.registry.ContainerRegistry;

public class PipeContainer extends Container {

    public final IWorldPosCallable worldPosCallable;
    public final PipePosition tilePos;

    public PipeContainer(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(id, playerInventory, IWorldPosCallable.NULL, PipePosition.createFromNBT(packetBuffer.readNbt()));
    }

    public PipeContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldCallable, PipePosition pos) {
        super(ContainerRegistry.PIPE_CONTAINER.get(), id);
        worldPosCallable = worldCallable;
        this.tilePos = pos;

        final int SLOT_DIFFERENCE = 18;
        final int posX = 16;
        final int posY = 113;
        final int posHotbarY = 171;

        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, 9 + row*9 + column, posX + column*SLOT_DIFFERENCE, posY + row*SLOT_DIFFERENCE));
            }
        }
        for(int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, posX + column*SLOT_DIFFERENCE, posHotbarY));
        }
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
