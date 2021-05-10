package software.bernie.techarium.util.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * <p>Utilities for working with {@link Container Inventory ContainerUtil}.</p>
 */
public final class ContainerUtil
{

	static final int PLAYER_INV_Y_DEFAULT = 84;
	static final int PLAYER_INV_X_DEFAULT = 8;
	private static final int SLOT_HEIGHT = 18;

	/**
	 * <p>Implementation for shift-clicking in ContainerUtil. This is a drop-in replacement you can call from the
	 * {@link Container#transferStackInSlot(PlayerEntity, int)} method in your Container.</p>
	 * <ul>
	 * <li>When the slot to be moved is not in the inventory of the player it will be moved there as known
	 * from vanilla inventories.</li>
	 * <li>When the slot to be moved is in the inventory of the player it will be moved to the first available Slot
	 * that accepts it.</li>
	 * </ul>
	 * your Container.</p>
	 *
	 * @param container the Container
	 * @param player    the player performing the shift-click
	 * @param slotIndex the slot being shift-clicked
	 * @return null if no transfer is possible
	 */
	public static ItemStack handleShiftClick(Container container, PlayerEntity player, int slotIndex) {
		@SuppressWarnings("unchecked")
		List<Slot> slots = container.inventorySlots;
		Slot sourceSlot = slots.get(slotIndex);
		ItemStack inputStack = sourceSlot.getStack();
		if (inputStack == ItemStack.EMPTY) {
			return ItemStack.EMPTY;
		}

		boolean sourceIsPlayer = sourceSlot.inventory == player.inventory;

		ItemStack copy = inputStack.copy();

		if (sourceIsPlayer) {
			if (container instanceof SpecialShiftClick) {
				ShiftClickTarget target = ((SpecialShiftClick) container).getShiftClickTarget(inputStack, player);
				if (!target.isStandard()) {
					if (target.isNone() || !mergeToTarget(player.inventory, sourceSlot, slots, target)) {
						return ItemStack.EMPTY;
					} else {
						return copy;
					}
				}
			}
			// transfer to any inventory
			if (!mergeStack(player.inventory, false, sourceSlot, slots, false)) {
				return ItemStack.EMPTY;
			} else {
				return copy;
			}
		} else {
			// transfer to player inventory
			// this is heuristic, but should do fine. if it doesn't the only "issue" is that vanilla behavior is not matched 100%
			boolean isMachineOutput = !sourceSlot.isItemValid(inputStack);
			if (!mergeStack(player.inventory, true, sourceSlot, slots, !isMachineOutput)) {
				return ItemStack.EMPTY;
			} else {
				return copy;
			}
		}
	}

	// same as mergeStack, but uses a ShiftClickTarget
	// ugly copy paste is ugly
	private static boolean mergeToTarget(PlayerInventory playerInv, Slot sourceSlot, List<Slot> slots, ShiftClickTarget target) {
		ItemStack sourceStack = sourceSlot.getStack();
		int originalSize = sourceStack.getCount();

		// first pass, try merge with existing stacks
		target.reset();
		while (sourceStack.getCount() > 0 && target.hasNext()) {
			Slot targetSlot = slots.get(target.next());
			if (targetSlot.inventory != playerInv) {
				ItemStack targetStack = targetSlot.getStack();
				if (ItemStacks.equal(targetStack, sourceStack)) {
					int targetMax = Math.min(targetSlot.getSlotStackLimit(), targetStack.getMaxStackSize());
					int toTransfer = Math.min(sourceStack.getCount(), targetMax - targetStack.getCount());
					if (toTransfer > 0) {
						targetStack.setCount(targetStack.getCount() + toTransfer);
						sourceStack.setCount(sourceStack.getCount() - toTransfer);
						targetSlot.onSlotChanged();
					}
				}
			}
		}
		if (sourceStack.getCount() == 0) {
			sourceSlot.putStack(ItemStack.EMPTY);
			return true;
		}

		//2nd pass: merge anything leftover into empty slots
		target.reset();
		while (target.hasNext()) {
			Slot targetSlot = slots.get(target.next());
			if (targetSlot.inventory != playerInv && !targetSlot.getHasStack() && targetSlot.isItemValid(sourceStack)) {
				int toPut = Math.min(targetSlot.getSlotStackLimit(), sourceStack.getCount());
				ItemStack newStack = sourceStack.copy();
				newStack.setCount(toPut);
				targetSlot.putStack(newStack);
				sourceStack.setCount(sourceStack.getCount() - toPut);
				sourceSlot.putStack(sourceStack);
				return true;
			}
		}
		if (originalSize != sourceStack.getCount()) {
			sourceSlot.onSlotChanged();
			return true;
		} else {
			return false;
		}
	}

	// returns true if it has found a target
	private static boolean mergeStack(PlayerInventory playerInv, boolean mergeIntoPlayer, Slot sourceSlot, List<Slot> slots, boolean reverse) {
		ItemStack sourceStack = sourceSlot.getStack();

		int originalSize = sourceStack.getCount();

		int len = slots.size();
		int idx;

		// first pass, try to merge with existing stacks
		// can skip this if stack is not stackable at all
		if (sourceStack.isStackable()) {
			idx = reverse ? len - 1 : 0;

			while (sourceStack.getCount() > 0 && (reverse ? idx >= 0 : idx < len)) {
				Slot targetSlot = slots.get(idx);
				if ((targetSlot.inventory == playerInv) == mergeIntoPlayer) {
					ItemStack target = targetSlot.getStack();
					if (ItemStacks.equal(sourceStack, target)) { // also checks target != null, because stack is never null
						int targetMax = Math.min(targetSlot.getSlotStackLimit(), target.getMaxStackSize());
						int toTransfer = Math.min(sourceStack.getCount(), targetMax - target.getCount());
						if (toTransfer > 0) {
							target.setCount(target.getCount() + toTransfer);
							sourceStack.setCount(sourceStack.getCount() - toTransfer);
							targetSlot.onSlotChanged();
						}
					}
				}

				if (reverse) {
					idx--;
				} else {
					idx++;
				}
			}
			if (sourceStack.getCount() == 0) {
				sourceSlot.putStack(ItemStack.EMPTY);
				return true;
			}
		}

		// 2nd pass: try to put anything remaining into a free slot
		idx = reverse ? len - 1 : 0;
		while (reverse ? idx >= 0 : idx < len) {
			Slot targetSlot = slots.get(idx);
			if ((targetSlot.inventory == playerInv) == mergeIntoPlayer
					&& !targetSlot.getHasStack() && targetSlot.isItemValid(sourceStack)) {
				int toPut = Math.min(targetSlot.getSlotStackLimit(), sourceStack.getCount());
				ItemStack newStack = sourceStack.copy();
				newStack.setCount(toPut);
				targetSlot.putStack(newStack);
				sourceStack.setCount(sourceStack.getCount() - toPut);
				sourceSlot.putStack(sourceStack);
				return true;
			}

			if (reverse) {
				idx--;
			} else {
				idx++;
			}
		}

		// we had success in merging only a partial stack
		if (sourceStack.getCount() != originalSize) {
			sourceSlot.onSlotChanged();
			return true;
		}
		return false;
	}

	private ContainerUtil() {
	}
}
