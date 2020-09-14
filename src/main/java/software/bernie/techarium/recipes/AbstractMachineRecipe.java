package software.bernie.techarium.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractMachineRecipe implements IRecipe<IInventory> {

    protected final ResourceLocation id;
    protected final IRecipeType<?> type;
    protected final int maxProgress;
    protected final int energyCost;

    protected AbstractMachineRecipe(ResourceLocation id, IRecipeType<?> type, int maxProgress, int energyCost) {
        this.id = id;
        this.type = type;
        this.maxProgress = maxProgress;
        this.energyCost = energyCost;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getEnergyCost() {
        return energyCost;
    }
}
