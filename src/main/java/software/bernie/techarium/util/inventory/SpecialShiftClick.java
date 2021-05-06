package software.bernie.techarium.util.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * <p>Can be implemented on your Container to provide non-standard behavior for
 *
 * @author diesieben07
 */
public interface SpecialShiftClick {

	/**
	 * <p>Get the target for the given ItemStack.</p>
	 *
	 * @param stack  the ItemStack being transferred
	 * @param player the player
	 * @return a ShiftClickTarget
	 */
	ShiftClickTarget getShiftClickTarget(ItemStack stack, PlayerEntity player);

}
