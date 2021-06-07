package software.bernie.techarium.recipes.recipe;

import com.google.gson.JsonObject;
import lombok.Builder;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.techarium.recipes.AbstractMachineRecipe;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.util.JsonCodecUtils;

import static software.bernie.techarium.registry.RecipeRegistry.EXCHANGE_STATION_RECIPE_TYPE;
import static software.bernie.techarium.registry.RecipeRegistry.EXCHANGE_STATION_SERIALIZER;


public class ExchangeStationRecipe extends AbstractMachineRecipe {

    private final ResourceLocation id;

    private final ItemStack input;

    private final ItemStack output;

    @Builder(buildMethodName = "construct")
    public ExchangeStationRecipe(ResourceLocation id, ItemStack input, ItemStack output) {
        super(id, EXCHANGE_STATION_RECIPE_TYPE, 0, 0, 0);
        this.id = id;
        this.input = input;
        this.output = output;
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

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.EXCHANGE_STATION_SERIALIZER.get();
    }

    @Override
    protected TechariumRecipeBuilder.Result getResult(ResourceLocation id) {
        return new Result(id);
    }

    public class Result extends AbstractMachineRecipe.Result {
        public Result(ResourceLocation id) {
            super(id);
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("itemIn", JsonCodecUtils.serialize(getInput()));
            json.add("itemOut", JsonCodecUtils.serialize(getOutput()));
        }
    }

    public ItemStack getInput()
    {
        return input;
    }

    public ItemStack getOutput()
    {
        return output;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
