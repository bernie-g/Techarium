package software.bernie.techarium.recipes;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.techarium.machine.interfaces.recipe.IMachineRecipe;
import software.bernie.techarium.recipes.recipe.TechariumRecipeBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMachineRecipe extends TechariumRecipeBuilder<AbstractMachineRecipe> implements IMachineRecipe {

    @Getter
    protected final ResourceLocation id;
    @Getter
    protected final IRecipeType<?> type;
    @Getter
    protected final int progressPerTick;
    @Getter
    protected final int maxProgress;
    @Getter
    protected final int rfPerTick;

    protected AbstractMachineRecipe(ResourceLocation id, IRecipeType<?> type, int progressPerTick, int maxProgress, int rfPerTick) {
        this.id = id;
        this.type = type;
        this.maxProgress = maxProgress;
        this.rfPerTick = rfPerTick;
        this.progressPerTick = progressPerTick;
    }


    @Override
    public List<Integer> getMaxProgressTimes() {
        List<Integer> progressList = new ArrayList<>();
        progressList.add(getMaxProgress());
        return progressList;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public abstract class Result extends TechariumRecipeBuilder.Result {
        public Result(ResourceLocation id) {
            super(id);
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("progressPerTick", AbstractMachineRecipe.this.progressPerTick);
            json.addProperty("maxProgress", AbstractMachineRecipe.this.maxProgress);
            json.addProperty("rfPerTick", AbstractMachineRecipe.this.rfPerTick);
        }
    }

}
