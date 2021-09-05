package software.bernie.techarium.datagen;

import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.datagen.base.TechariumRecipeProviderBase;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.recipe.recipe.ExchangeStationRecipe;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.registry.TagRegistry;
import software.bernie.techarium.util.loot.ChancedItemStack;
import software.bernie.techarium.util.loot.ChancedItemStackList;

import java.util.function.Consumer;

public class TechariumRecipeProvider extends TechariumRecipeProviderBase {
    public TechariumRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        registerVanillaBotariumRecipes(consumer);

        registerExchangeStationRecipes(consumer);

        registerVanillaArboretumRecipes(consumer);
        
        registerGravMagnetRecipes(consumer);

        registerSmeltingRecipes(consumer);
        registerCraftingRecipes(consumer);
        ModIntegrations.getIntegrations().forEach(wrapper -> wrapper.get().ifPresent(o -> o.generateRecipes(consumer)));
    }

    private void registerCraftingRecipes(Consumer<IFinishedRecipe> consumer) {
        buildMetalRecipe("aluminium", ItemRegistry.ALUMINIUM_INGOT.get(), ItemRegistry.ALUMINIUM_NUGGET
                .get(), BlockRegistry.ALUMINIUM_BLOCK.get(), consumer);
        buildMetalRecipe("copper", ItemRegistry.COPPER_INGOT.get(), ItemRegistry.COPPER_NUGGET
                .get(), BlockRegistry.COPPER_BLOCK.get(), consumer);
        buildMetalRecipe("lead", ItemRegistry.LEAD_INGOT.get(), ItemRegistry.LEAD_NUGGET.get(), BlockRegistry.LEAD_BLOCK
                .get(), consumer);
        buildMetalRecipe("nickel", ItemRegistry.NICKEL_INGOT.get(), ItemRegistry.NICKEL_NUGGET
                .get(), BlockRegistry.NICKEL_BLOCK.get(), consumer);
        buildMetalRecipe("nickel", ItemRegistry.ZINC_INGOT.get(), ItemRegistry.ZINC_NUGGET
                .get(), BlockRegistry.ZINC_BLOCK.get(), consumer);
    }

    private void registerVanillaBotariumRecipes(Consumer<IFinishedRecipe> consumer) {
        buildBotariumFarmlandRecipe(Items.CARROT, ChancedItemStackList.of(Items.CARROT), 1000, 800, consumer);
        buildBotariumRecipe(Items.COCOA_BEANS, ChancedItemStackList.of(Items.COCOA_BEANS), Ingredient
                .of(Items.JUNGLE_LOG), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(Items.POTATO, ChancedItemStackList.of(Items.POTATO), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(Items.WHEAT_SEEDS, ChancedItemStackList.of(Items.WHEAT), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(Items.BEETROOT_SEEDS, ChancedItemStackList.of(Items.BEETROOT), 1000, 800, consumer);
        buildBotariumRecipe(Items.SWEET_BERRIES, ChancedItemStackList.of(Items.SWEET_BERRIES), 1000, 800, consumer);

        buildBotariumRecipe(Items.SUGAR_CANE, ChancedItemStackList.of(Items.SUGAR_CANE), Ingredient
                .of(Items.SAND), 1000, 600, consumer);
        buildBotariumRecipe(Items.CACTUS, ChancedItemStackList.of(Items.CACTUS), Ingredient
                .of(Items.SAND), 1000, 600, consumer);

        buildBotariumRecipe(Items.NETHER_WART, ChancedItemStackList.of(Items.NETHER_WART), Ingredient
                .of(Blocks.SOUL_SAND), null,new FluidStack(Fluids.LAVA, 50), 1000, consumer);

        buildBotariumRecipe(Items.RED_MUSHROOM, ChancedItemStackList.of(Items.RED_MUSHROOM), Ingredient
                .of(Items.STONE, Items.MYCELIUM), 50, 1000, consumer);
        buildBotariumRecipe(Items.BROWN_MUSHROOM, ChancedItemStackList.of(Items.BROWN_MUSHROOM), Ingredient
                .of(Items.STONE, Items.MYCELIUM), 50, 1000, consumer);

        buildBotariumFarmlandRecipe(Items.MELON_SEEDS, ChancedItemStackList.of(Items.MELON), 1500, 1200, consumer);
        buildBotariumFarmlandRecipe(Items.PUMPKIN_SEEDS, ChancedItemStackList.of(Items.PUMPKIN), 1500, 1200, consumer);

        buildBotariumRecipe(Items.KELP, ChancedItemStackList.of(Items.KELP), Ingredient.of(TagRegistry.DIRT), (BlockItem) Items.DIRT, 3000, 1000, consumer);

        buildBotariumFlowerRecipe((FlowerBlock) Blocks.DANDELION, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.POPPY, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.BLUE_ORCHID, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.ALLIUM, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.AZURE_BLUET, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.RED_TULIP, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.ORANGE_TULIP, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.WHITE_TULIP, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.PINK_TULIP, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.OXEYE_DAISY, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.CORNFLOWER, consumer);
        buildBotariumFlowerRecipe((FlowerBlock) Blocks.LILY_OF_THE_VALLEY, consumer);

    }

    private void registerGravMagnetRecipes(Consumer<IFinishedRecipe> consumer) {
    	buildGravMagnetRecipe("eye_of_ender", 
    			ChancedItemStackList.of(
    					ChancedItemStack.of(Items.ENDER_PEARL, 1, 1d),
    					ChancedItemStack.of(Items.BLAZE_POWDER, 1, 1d)), new ItemStack(Items.ENDER_EYE), 6 * 20, true, consumer);
    }

    private void registerExchangeStationRecipes(Consumer<IFinishedRecipe> consumer) {
        buildExchangeStationRecipe(Items.GOLD_INGOT, 1, Items.DIRT, 1, consumer);
        buildExchangeStationRecipe(Items.GOLD_INGOT, 3, Items.LIME_CONCRETE, 1, consumer);
        buildExchangeStationRecipe(Items.GOLD_INGOT, 5, Items.ORANGE_BANNER, 1, consumer);
        buildExchangeStationRecipe(Items.GOLD_INGOT, 7, Items.BARRIER, 1, consumer);
        buildExchangeStationRecipe(Items.GOLD_INGOT, 9, Items.JUNGLE_SAPLING, 1, consumer);
        buildExchangeStationRecipe(Items.GOLD_INGOT, 11, Items.NETHER_STAR, 1, consumer);
        buildExchangeStationRecipe(Items.GOLD_INGOT, 13, Items.FIREWORK_ROCKET, 1, consumer);
        buildExchangeStationRecipe(Items.GOLD_INGOT, 7, Items.LEVER, 1, consumer);
    }

    private void registerVanillaArboretumRecipes(Consumer<IFinishedRecipe> consumer) {
        buildArboretumRecipe(Items.OAK_SAPLING, ChancedItemStackList
                .of(ChancedItemStack.of(Items.OAK_LOG, 6, 0.5), ChancedItemStack
                        .of(Items.OAK_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.SPRUCE_SAPLING, ChancedItemStackList
                .of(ChancedItemStack.of(Items.SPRUCE_LOG, 6, 0.5), ChancedItemStack
                        .of(Items.SPRUCE_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.ACACIA_SAPLING, ChancedItemStackList
                .of(ChancedItemStack.of(Items.ACACIA_LOG, 6, 0.5), ChancedItemStack
                        .of(Items.ACACIA_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.BIRCH_SAPLING, ChancedItemStackList
                .of(ChancedItemStack.of(Items.BIRCH_LOG, 6, 0.5), ChancedItemStack
                        .of(Items.BIRCH_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.JUNGLE_SAPLING, ChancedItemStackList
                .of(ChancedItemStack.of(Items.JUNGLE_LOG, 6, 0.5), ChancedItemStack
                        .of(Items.JUNGLE_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.DARK_OAK_SAPLING, ChancedItemStackList
                .of(ChancedItemStack.of(Items.DARK_OAK_LOG, 6, 0.5), ChancedItemStack
                        .of(Items.DARK_OAK_SAPLING)), 1000, 1000, consumer);
    }

    private void registerSmeltingRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder
                .smelting(Ingredient.of(BlockRegistry.ALUMINIUM_ORE.get()), ItemRegistry.ALUMINIUM_INGOT
                        .get(), 0.7f, 200)
                .unlockedBy("has_item", has(BlockRegistry.ALUMINIUM_ORE.get()))
                .save(consumer, Techarium.rl("smelting/aluminium_ore"));
        CookingRecipeBuilder
                .smelting(Ingredient.of(BlockRegistry.COPPER_ORE.get()), ItemRegistry.COPPER_INGOT.get(), 0.7f, 200)
                .unlockedBy("has_item", has(BlockRegistry.COPPER_ORE.get()))
                .save(consumer, Techarium.rl("smelting/copper_ore"));
        CookingRecipeBuilder
                .smelting(Ingredient.of(BlockRegistry.LEAD_ORE.get()), ItemRegistry.LEAD_INGOT.get(), 0.7f, 200)
                .unlockedBy("has_item", has(BlockRegistry.LEAD_ORE.get()))
                .save(consumer, Techarium.rl("smelting/lead_ore"));
        CookingRecipeBuilder
                .smelting(Ingredient.of(BlockRegistry.NICKEL_ORE.get()), ItemRegistry.NICKEL_INGOT.get(), 0.7f, 200)
                .unlockedBy("has_item", has(BlockRegistry.NICKEL_ORE.get()))
                .save(consumer, Techarium.rl("smelting/nickel_ore"));
        CookingRecipeBuilder
                .smelting(Ingredient.of(BlockRegistry.ZINC_ORE.get()), ItemRegistry.ZINC_INGOT.get(), 0.7f, 200)
                .unlockedBy("has_item", has(BlockRegistry.ZINC_ORE.get()))
                .save(consumer, Techarium.rl("smelting/zinc_ore"));

        CookingRecipeBuilder
                .blasting(Ingredient.of(BlockRegistry.ALUMINIUM_ORE.get()), ItemRegistry.ALUMINIUM_INGOT
                        .get(), 0.7f, 100)
                .unlockedBy("has_item", has(BlockRegistry.ALUMINIUM_ORE.get()))
                .save(consumer, Techarium.rl("blasting/aluminium_ore"));
        CookingRecipeBuilder
                .blasting(Ingredient.of(BlockRegistry.COPPER_ORE.get()), ItemRegistry.COPPER_INGOT.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BlockRegistry.COPPER_ORE.get()))
                .save(consumer, Techarium.rl("blasting/copper_ore"));
        CookingRecipeBuilder
                .blasting(Ingredient.of(BlockRegistry.LEAD_ORE.get()), ItemRegistry.LEAD_INGOT.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BlockRegistry.LEAD_ORE.get()))
                .save(consumer, Techarium.rl("blasting/lead_ore"));
        CookingRecipeBuilder
                .blasting(Ingredient.of(BlockRegistry.NICKEL_ORE.get()), ItemRegistry.NICKEL_INGOT.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BlockRegistry.NICKEL_ORE.get()))
                .save(consumer, Techarium.rl("blasting/nickel_ore"));
        CookingRecipeBuilder
                .blasting(Ingredient.of(BlockRegistry.ZINC_ORE.get()), ItemRegistry.ZINC_INGOT.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BlockRegistry.ZINC_ORE.get()))
                .save(consumer, Techarium.rl("blasting/zinc_ore"));
    }

    private static void buildExchangeStationRecipe(IItemProvider input, int inputAmount, IItemProvider output, int outputAmount, Consumer<IFinishedRecipe> consumer) {
        ExchangeStationRecipe.builder()
                .input(new ItemStack(input, inputAmount))
                .output(new ItemStack(output, outputAmount))
                .construct()
                .build(consumer,
                        Techarium.rl(
                                "exchange_station/" + output.asItem().getRegistryName().getNamespace() + "/" + output.asItem().getRegistryName().getPath()));
    }
}
