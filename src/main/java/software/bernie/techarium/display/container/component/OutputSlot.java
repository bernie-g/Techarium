package software.bernie.techarium.display.container.component;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class OutputSlot extends Slot{

	public OutputSlot(IInventory inv, int id, int posX, int posY) {
		super(inv, id, posX, posY);
	}
	
	@Override
	public boolean mayPlace(ItemStack p_75214_1_) {
		return false;
	}

}
