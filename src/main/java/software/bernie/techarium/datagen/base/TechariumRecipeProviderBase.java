package software.bernie.techarium.datagen.base;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.recipe.recipe.ArboretumRecipe;
import software.bernie.techarium.recipe.recipe.BotariumRecipe;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;
import software.bernie.techarium.recipe.recipe.assembler.AssemblerRecipe;
import software.bernie.techarium.recipe.recipe.assembler.AssemblerRecipePatern;
import software.bernie.techarium.registry.TagRegistry;
import software.bernie.techarium.util.ChancedItemStackList;

import java.util.function.Consumer;

public abstract class TechariumRecipeProviderBase extends ForgeRecipeProvider {
    public TechariumRecipeProviderBase(DataGenerator generatorIn) {
        super(generatorIn);
    }

    public void buildBotariumFlowerRecipe(FlowerBlock flowerBlock, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(flowerBlock.asItem(), ChancedItemStackList.of(new ItemStack(flowerBlock)), Ingredient
                .of(Blocks.GRASS_BLOCK), 1000, 1000, consumer);
    }
    public void buildBotariumRecipe(Item seed, ChancedItemStackList drop, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, Ingredient.of(TagRegistry.DIRT), null, amountWater, time, consumer);
    }
    public void buildBotariumFarmlandRecipe(Item seed, ChancedItemStackList drop, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, Ingredient.of(TagRegistry.DIRT), (BlockItem) Items.FARMLAND, amountWater, time, consumer);
    }
    public void buildBotariumRecipe(Item seed, ChancedItemStackList drop, Ingredient soil, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, soil, null, new FluidStack(Fluids.WATER, amountWater), time, consumer);
    }
    public void buildBotariumRecipe(Item seed, ChancedItemStackList drop, Ingredient soil, BlockItem renderSoil, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, soil, renderSoil, new FluidStack(Fluids.WATER, amountWater), time, consumer);
    }

    public void buildBotariumRecipe(Item seed, ChancedItemStackList drop, Ingredient soil, BlockItem renderSoil, FluidStack fluid, int time, Consumer<IFinishedRecipe> consumer) {
        BotariumRecipe.builder()
                .cropType(Ingredient.of(seed))
                .soilIn(soil)
                .renderSoil(renderSoil)
                .fluidIn(fluid)
                .maxProgress(time)
                .rfPerTick(10)
                .progressPerTick(1)
                .output(drop)
                .construct()
                .build(consumer,
                        new ResourceLocation(Techarium.ModID,
                                "botarium/" + seed.getRegistryName().getNamespace() + "/" + seed.getRegistryName()
                                        .getPath()));
    }

    public void buildArboretumRecipe(Item sapling, ChancedItemStackList drop, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildArboretumRecipe(sapling, drop, Ingredient.of(TagRegistry.DIRT), amountWater, time, consumer);
    }

    public void buildArboretumRecipe(Item sapling, ChancedItemStackList drop, Ingredient soil, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildArboretumRecipe(sapling, drop, soil, new FluidStack(Fluids.WATER, amountWater), time, consumer);
    }

    public void buildArboretumRecipe(Item sapling, ChancedItemStackList drop, Ingredient soil, FluidStack fluid, int time, Consumer<IFinishedRecipe> consumer) {
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
                                "arboretum/" + sapling.getRegistryName().getNamespace() + "/" + sapling
                                        .getRegistryName().getPath()));
    }

    public void buildGravMagnetRecipe(String name, ChancedItemStackList output, ItemStack input, int processTime, boolean pull, Consumer<IFinishedRecipe> consumer) {
        GravMagnetRecipe.builder()
        	.output(output)
        	.input(input)
        	.processTime(processTime)
        	.pull(pull)
        	.construct()
        	.build(consumer, new ResourceLocation(Techarium.ModID, "gravmagnet/" + name));
    }
    
    public void buildAssemblerRecipe(String name, AssemblerRecipePatern patern, ItemStack output, boolean isShapeless, Consumer<IFinishedRecipe> consumer) {
    	AssemblerRecipe.builder()
    		.recipePatern(patern)
        	.output(output)
    		.isShapeless(isShapeless)
    		.construct()
    		.build(consumer, new ResourceLocation(Techarium.ModID, "assembler/" + name));
    }
    
    public void buildMetalRecipe(String name, Item ingot, Item nugget, Block block, Consumer<IFinishedRecipe> consumer) {
        String nuggetName = nugget.getRegistryName().getPath();
        String ingotName = ingot.getRegistryName().getPath();
        String blockName = block.getRegistryName().getPath();
        ShapedRecipeBuilder.shaped(ingot).define('#', nugget).pattern("###").pattern("###")
                .pattern("###").group(ingotName).unlockedBy("has_" + nuggetName, has(nugget))
                .save(consumer, Techarium.rl(name + "/" + ingotName + "_from_" + ingotName));
        ShapelessRecipeBuilder.shapeless(nugget, 9).requires(ingot).unlockedBy("has_" + ingotName, has(ingot))
                .save(consumer, Techarium.rl(name + "/" + nuggetName + "_from_" + ingotName));
        ShapelessRecipeBuilder.shapeless(ingot, 9).requires(block).group(ingotName)
                .unlockedBy("has_" + blockName, has(block))
                .save(consumer, Techarium.rl(name + "/" + ingotName + "_from_" + blockName));
        ShapedRecipeBuilder.shaped(block).define('#', ingot).pattern("###").pattern("###")
                .pattern("###").unlockedBy("has_" + ingotName, has(ingot))
                .save(consumer, Techarium.rl(name + "/" + blockName + "_from_" + ingotName));
    }
}
