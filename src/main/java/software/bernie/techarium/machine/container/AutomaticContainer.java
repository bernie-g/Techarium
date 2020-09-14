package software.bernie.techarium.machine.container;

import javafx.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.tile.base.MachineMasterTile;

import static software.bernie.techarium.registry.ContainerRegistry.AUTO_CONTAINER;

public class AutomaticContainer extends Container {

    protected MachineMasterTile<?> tile;

    private PlayerInventory inv;

    private ITextComponent name;

    private final BlockPos tileLocation;

    public AutomaticContainer(MachineMasterTile<?> tile, PlayerInventory inv, int id, ITextComponent containerName) {
        super(AUTO_CONTAINER.get(), id);
        this.inv = inv;
        this.name = containerName;
        this.tile = tile;
        this.tileLocation = tile.getPos();

        getMachineController().getContainerComponents().forEach(component -> this.addSlot(component.create()));

        for (Integer slot : getMachineController().getPlayerHotBarSlotsXY().keySet()) {
            Pair<Integer, Integer> slotXY = getMachineController().getPlayerHotBarSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getKey(), slotXY.getValue()));
        }

        for (Integer slot : getMachineController().getPlayerInvSlotsXY().keySet()) {
            Pair<Integer, Integer> slotXY = getMachineController().getPlayerInvSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getKey(), slotXY.getValue()));
        }
    }

    public AutomaticContainer(int id, PlayerInventory inv, PacketBuffer packetBuffer) {
        super(AUTO_CONTAINER.get(), id);
        tileLocation = packetBuffer.readBlockPos();
        this.name = packetBuffer.readTextComponent();
        this.tile = (MachineMasterTile<?>) inv.player.world.getTileEntity(tileLocation);
        getMachineController().getContainerComponents().forEach(component -> this.addSlot(component.create()));

        for (Integer slot : getMachineController().getPlayerHotBarSlotsXY().keySet()) {
            Pair<Integer, Integer> slotXY = getMachineController().getPlayerHotBarSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getKey(), slotXY.getValue()));
        }

        for (Integer slot : getMachineController().getPlayerInvSlotsXY().keySet()) {
            Pair<Integer, Integer> slotXY = getMachineController().getPlayerInvSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getKey(), slotXY.getValue()));
        }
    }

    private void addPlayerInventories(){

    }

    public MachineController<?> getMachineController() {
        return tile.getController().getActiveController();
    }

    public ITextComponent getContainerName() {
        return name;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerIn.getDistanceSq((double) tileLocation.getX() + 0.5D, (double) tileLocation.getY() + 0.5D, (double) tileLocation.getZ() + 0.5D) <= 64.0D;
    }
}
