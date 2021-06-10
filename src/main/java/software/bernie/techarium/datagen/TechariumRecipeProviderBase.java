package software.bernie.techarium.datagen;

import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.recipes.recipe.ArboretumRecipe;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.TagRegistry;
import software.bernie.techarium.util.ChancedItemStackList;

import java.util.function.Consumer;

public abstract class TechariumRecipeProviderBase extends ForgeRecipeProvider {
    public TechariumRecipeProviderBase(DataGenerator generatorIn) {
        super(generatorIn);
    }

    void buildBotariumFlowerRecipe(FlowerBlock flowerBlock, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(flowerBlock.asItem(), ChancedItemStackList.of(new ItemStack(flowerBlock)), Ingredient.of(Blocks.GRASS_BLOCK), 1000, 1000, consumer);
    }

    void buildBotariumRecipe(Item seed, ChancedItemStackList drop, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, Ingredient.of(TagRegistry.DIRT), amountWater, time, consumer);
    }

    void buildBotariumRecipe(Item seed, ChancedItemStackList drop, Ingredient soil, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, soil, new FluidStack(Fluids.WATER, amountWater), time, consumer);
    }

    void buildBotariumRecipe(Item seed, ChancedItemStackList drop, Ingredient soil, FluidStack fluid, int time, Consumer<IFinishedRecipe> consumer) {
        BotariumRecipe.builder()
                .cropType(Ingredient.of(seed))
                .soilIn(soil)
                .fluidIn(fluid)
                .maxProgress(time)
                .rfPerTick(10)
                .progressPerTick(1)
                .output(drop)
                .construct()
                .build(consumer,
                        new ResourceLocation(Techarium.ModID,
                                "botarium/" + seed.getRegistryName().getNamespace() + "/" + seed.getRegistryName().getPath()));
    }

    void buildArboretumRecipe(Item sapling, ChancedItemStackList drop, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildArboretumRecipe(sapling, drop, Ingredient.of(TagRegistry.DIRT), amountWater, time, consumer);
    }

    void buildArboretumRecipe(Item sapling, ChancedItemStackList drop, Ingredient soil, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildArboretumRecipe(sapling, drop, soil, new FluidStack(Fluids.WATER, amountWater), time, consumer);
    }

    void buildArboretumRecipe(Item sapling, ChancedItemStackList drop, Ingredient soil, FluidStack fluid, int time, Consumer<IFinishedRecipe> consumer) {
        ArboretumRecipe.builder()
                .cropType(Ingredient.of(sapling))
                .soilIn(soil)
                .fluidIn(fluid)
                .maxProgress(time)
                .rfPerTick(10)
                .progressPerTick(1)
                .output(drop)
                .construct()
                .build(consumer,
                        new ResourceLocation(Techarium.ModID,
                                "arboretum/" + sapling.getRegistryName().getNamespace() + "/" + sapling.getRegistryName().getPath()));
    }
}
