package software.bernie.techarium.machine.interfaces.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

public interface IRecipeMachine<T extends IRecipe<IInventory>> {

    boolean shouldCheckForRecipe();

    boolean checkRecipe(IRecipe<?> recipe);

    default T castRecipe(IRecipe<?> iRecipe) {
        return (T)iRecipe;
    }

    Class<T> getRecipeClass();

    boolean matchRecipe(T currentRecipe);

    void handleProgressFinish(T currentRecipe);
}
