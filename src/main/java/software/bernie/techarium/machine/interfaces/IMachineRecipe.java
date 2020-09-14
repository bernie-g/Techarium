package software.bernie.techarium.machine.interfaces;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public interface IMachineRecipe extends IRecipe<IInventory> {

    List<Integer> getMaxProgressTimes();


}
