package software.bernie.techarium.machine.interfaces;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

public interface IRecipeMachine<T extends IRecipe<IInventory>> {

    boolean shouldCheckForRecipe();

    boolean checkRecipe(IRecipe<?> recipe);

    T castRecipe(IRecipe<?> iRecipe);

    boolean matchRecipe(T currentRecipe);

    void handleProgressFinish(T currentRecipe);
}
