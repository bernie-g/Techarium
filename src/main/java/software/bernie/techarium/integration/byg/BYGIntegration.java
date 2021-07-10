package software.bernie.techarium.integration.byg;

import static corgiaoc.byg.core.BYGBlocks.*;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import software.bernie.techarium.integration.Integration;
import software.bernie.techarium.util.ChancedItemStack;
import software.bernie.techarium.util.ChancedItemStackList;

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
        saplings.put(LAMENT_LOG, LAMENT_SAPLING);
        saplings.put(ASPEN_LOG, ASPEN_SAPLING);
        saplings.put(BAOBAB_LOG, BAOBAB_SAPLING);
        saplings.put(CHERRY_LOG, PINK_CHERRY_SAPLING);
        saplings.put(CHERRY_LOG, WHITE_CHERRY_SAPLING);
        saplings.put(ARAUCARIA_LEAVES, ARAUCARIA_SAPLING);
        saplings.put(CYPRESS_LOG, CYPRESS_SAPLING);
        saplings.put(EBONY_LOG, EBONY_SAPLING);
        saplings.put(CIKA_LOG, CIKA_SAPLING);
        saplings.put(ETHER_LOG, ETHER_SAPLING);
        saplings.put(FIR_LOG, FIR_SAPLING);
        saplings.put(HOLLY_LOG, HOLLY_SAPLING);
        saplings.put(JACARANDA_LOG, JACARANDA_SAPLING);
        saplings.put(MAHOGANY_LOG, MAHOGANY_SAPLING);
        saplings.put(MAPLE_LOG, MAPLE_SAPLING);
        saplings.put(PALM_LOG, PALM_SAPLING);
        saplings.put(NIGHTSHADE_LOG, NIGHTSHADE_SAPLING);
        saplings.put(REDWOOD_LOG, REDWOOD_SAPLING);
        saplings.put(SKYRIS_LOG, SKYRIS_SAPLING);
        saplings.put(WILLOW_LOG, WILLOW_SAPLING);
        saplings.put(ZELKOVA_LOG, ZELKOVA_SAPLING);
        saplings.put(MANGROVE_LOG, MANGROVE_SAPLING);
        saplings.put(PINE_LOG, PINE_SAPLING);
        saplings.put(BLUE_ENCHANTED_LOG, BLUE_ENCHANTED_SAPLING);
        saplings.put(GREEN_ENCHANTED_LOG, GREEN_ENCHANTED_SAPLING);
        saplings.put(PALO_VERDE_LOG, PALO_VERDE_SAPLING);
        saplings.put(WITCH_HAZEL_LOG, WITCH_HAZEL_SAPLING);
        saplings.put(RAINBOW_EUCALYPTUS_LOG, RAINBOW_EUCALYPTUS_SAPLING);
        saplings.put(WITHERING_OAK_LOG, WITHERING_OAK_SAPLING);
        for (Map.Entry<Block, Block> entry : saplings.entrySet()) {
            buildArboretumRecipe(entry.getValue().asItem(), ChancedItemStackList.of(ChancedItemStack.of(entry.getKey().asItem(), 6, 0.5), ChancedItemStack.of(entry.getValue().asItem())), 1000, 1000, consumer);
        }
    }
}
