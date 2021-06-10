package software.bernie.techarium.datagen;

import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.recipes.recipe.ArboretumRecipe;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.registry.TagRegistry;
import software.bernie.techarium.util.ChancedItemStack;
import software.bernie.techarium.util.ChancedItemStackList;

import java.util.function.Consumer;

public class TechariumRecipeProvider extends TechariumRecipeProviderBase {
    public TechariumRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        registerVanillaBotariumRecipes(consumer);
        registerVanillaArboretumRecipes(consumer);
        ModIntegrations.getIntegrations().forEach(wrapper -> wrapper.get().ifPresent(o -> o.generateRecipes(consumer)));
    }

    private void registerVanillaBotariumRecipes(Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(Items.CARROT, ChancedItemStackList.of(Items.CARROT), 1000, 800, consumer);
        buildBotariumRecipe(Items.COCOA_BEANS, ChancedItemStackList.of(Items.COCOA_BEANS), Ingredient.of(Items.JUNGLE_LOG), 1000, 800, consumer);
        buildBotariumRecipe(Items.POTATO, ChancedItemStackList.of(Items.POTATO), 1000, 800, consumer);
        buildBotariumRecipe(Items.WHEAT_SEEDS, ChancedItemStackList.of(Items.WHEAT), 1000, 800, consumer);
        buildBotariumRecipe(Items.BEETROOT_SEEDS, ChancedItemStackList.of(Items.BEETROOT), 1000, 800, consumer);
        buildBotariumRecipe(Items.SWEET_BERRIES, ChancedItemStackList.of(Items.SWEET_BERRIES), 1000, 800, consumer);

        buildBotariumRecipe(Items.SUGAR_CANE, ChancedItemStackList.of(Items.SUGAR_CANE), Ingredient.of(Items.SAND), 1000, 600, consumer);
        buildBotariumRecipe(Items.CACTUS, ChancedItemStackList.of(Items.CACTUS), Ingredient.of(Items.SAND), 1000, 600, consumer);

        buildBotariumRecipe(Items.NETHER_WART, ChancedItemStackList.of(Items.NETHER_WART), Ingredient.of(Blocks.SOUL_SAND), new FluidStack(Fluids.LAVA, 50), 1000, consumer);

        buildBotariumRecipe(Items.RED_MUSHROOM, ChancedItemStackList.of(Items.RED_MUSHROOM), Ingredient.of(Items.STONE, Items.MYCELIUM), 50, 1000, consumer);
        buildBotariumRecipe(Items.BROWN_MUSHROOM, ChancedItemStackList.of(Items.BROWN_MUSHROOM), Ingredient.of(Items.STONE, Items.MYCELIUM), 50, 1000, consumer);

        buildBotariumRecipe(Items.MELON_SEEDS, ChancedItemStackList.of(Items.MELON), 1500, 1200, consumer);
        buildBotariumRecipe(Items.PUMPKIN_SEEDS, ChancedItemStackList.of(Items.PUMPKIN), 1500, 1200, consumer);

        buildBotariumRecipe(Items.KELP, ChancedItemStackList.of(Items.KELP), 3000, 1000, consumer);

        buildBotariumFlowerRecipe((FlowerBlock)Blocks.DANDELION, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.POPPY, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.BLUE_ORCHID, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.ALLIUM, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.AZURE_BLUET, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.RED_TULIP, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.ORANGE_TULIP, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.WHITE_TULIP, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.PINK_TULIP, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.OXEYE_DAISY, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.CORNFLOWER, consumer);
        buildBotariumFlowerRecipe((FlowerBlock)Blocks.LILY_OF_THE_VALLEY, consumer);

    }

    private void registerVanillaArboretumRecipes(Consumer<IFinishedRecipe> consumer) {
        buildArboretumRecipe(Items.OAK_SAPLING, ChancedItemStackList.of(ChancedItemStack.of(Items.OAK_LOG, 6, 0.5), ChancedItemStack.of(Items.OAK_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.SPRUCE_SAPLING, ChancedItemStackList.of(ChancedItemStack.of(Items.SPRUCE_LOG, 6, 0.5), ChancedItemStack.of(Items.SPRUCE_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.ACACIA_SAPLING, ChancedItemStackList.of(ChancedItemStack.of(Items.ACACIA_LOG, 6, 0.5), ChancedItemStack.of(Items.ACACIA_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.BIRCH_SAPLING, ChancedItemStackList.of(ChancedItemStack.of(Items.BIRCH_LOG, 6, 0.5), ChancedItemStack.of(Items.BIRCH_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.JUNGLE_SAPLING, ChancedItemStackList.of(ChancedItemStack.of(Items.JUNGLE_LOG, 6, 0.5), ChancedItemStack.of(Items.JUNGLE_SAPLING)), 1000, 1000, consumer);
        buildArboretumRecipe(Items.DARK_OAK_SAPLING, ChancedItemStackList.of(ChancedItemStack.of(Items.DARK_OAK_LOG, 6, 0.5), ChancedItemStack.of(Items.DARK_OAK_SAPLING)), 1000, 1000, consumer);
    }

    private void registerSmeltingRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.ALUMINIUM_ORE.get()), ItemRegistry.ALUMINIUM_INGOT.get(), 0.7f, 200)
                .unlockedBy("has_item", has(Items.COBBLESTONE))
                .save(consumer, Techarium.rl("smelting/ender_ore"));
        CookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.COPPER_ORE.get()), ItemRegistry.COPPER_INGOT.get(), 0.7f, 200)
                .unlockedBy("has_item", has(Items.COBBLESTONE))
                .save(consumer, Techarium.rl("smelting/ender_ore"));
        CookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.LEAD_ORE.get()), ItemRegistry.LEAD_INGOT.get(), 0.7f, 200)
                .unlockedBy("has_item", has(Items.COBBLESTONE))
                .save(consumer, Techarium.rl("smelting/ender_ore"));
    }
}
