package software.bernie.techarium.integration.byg;

import static corgiaoc.byg.core.BYGBlocks.*;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import software.bernie.techarium.integration.Integration;
import software.bernie.techarium.util.loot.ChancedItemStack;
import software.bernie.techarium.util.loot.ChancedItemStackList;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BYGIntegration extends Integration {
    public BYGIntegration(String modID) {
        super(modID);
    }

    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        Map<Block, Block> saplings = new HashMap<>();
        saplings.put(LAMENT_SAPLING, LAMENT_LOG);
        saplings.put(ASPEN_SAPLING, ASPEN_LOG);
        saplings.put(BAOBAB_SAPLING, BAOBAB_LOG);
        saplings.put(PINK_CHERRY_SAPLING, CHERRY_LOG);
        saplings.put(WHITE_CHERRY_SAPLING, CHERRY_LOG);
        saplings.put(ARAUCARIA_SAPLING, PINE_LOG);
        saplings.put(CYPRESS_SAPLING, CYPRESS_LOG);
        saplings.put(EBONY_SAPLING, EBONY_LOG);
        saplings.put(CIKA_SAPLING, CIKA_LOG);
        saplings.put(ETHER_SAPLING, ETHER_LOG);
        saplings.put(FIR_SAPLING, FIR_LOG);
        saplings.put(HOLLY_SAPLING, HOLLY_LOG);
        saplings.put(JACARANDA_SAPLING, JACARANDA_LOG);
        saplings.put(MAHOGANY_SAPLING, MAHOGANY_LOG);
        saplings.put(MAPLE_SAPLING, MAPLE_LOG);
        saplings.put(PALM_SAPLING, PALM_LOG);
        saplings.put(NIGHTSHADE_SAPLING, NIGHTSHADE_LOG);
        saplings.put(REDWOOD_SAPLING, REDWOOD_LOG);
        saplings.put(SKYRIS_SAPLING, SKYRIS_LOG);
        saplings.put(WILLOW_SAPLING, WILLOW_LOG);
        saplings.put(ZELKOVA_SAPLING, ZELKOVA_LOG);
        saplings.put(MANGROVE_SAPLING, MANGROVE_LOG);
        saplings.put(PINE_SAPLING, PINE_LOG);
        saplings.put(BLUE_ENCHANTED_SAPLING, BLUE_ENCHANTED_LOG);
        saplings.put(GREEN_ENCHANTED_SAPLING, GREEN_ENCHANTED_LOG);
        saplings.put(PALO_VERDE_SAPLING, PALO_VERDE_LOG);
        saplings.put(WITCH_HAZEL_SAPLING, WITCH_HAZEL_LOG);
        saplings.put(RAINBOW_EUCALYPTUS_SAPLING, RAINBOW_EUCALYPTUS_LOG);
        saplings.put(WITHERING_OAK_SAPLING, WITHERING_OAK_LOG);
        for (Map.Entry<Block, Block> entry : saplings.entrySet()) {
            buildArboretumRecipe(entry.getKey().asItem(), ChancedItemStackList.of(ChancedItemStack.of(entry.getValue().asItem(), 6, 0.5), ChancedItemStack.of(entry.getKey().asItem())), 1000, 1000, consumer);
        }
    }
}
