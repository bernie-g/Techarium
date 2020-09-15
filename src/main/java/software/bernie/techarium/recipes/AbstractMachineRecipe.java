package software.bernie.techarium.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.techarium.machine.interfaces.recipe.IMachineRecipe;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMachineRecipe implements IMachineRecipe {

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

    @Override
    public List<Integer> getMaxProgressTimes() {
        List<Integer> progressList = new ArrayList<>();
        progressList.add(getMaxProgress());
        return progressList;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

}
