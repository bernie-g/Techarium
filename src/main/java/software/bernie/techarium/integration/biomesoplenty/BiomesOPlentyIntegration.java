package software.bernie.techarium.integration.biomesoplenty;

import static biomesoplenty.api.block.BOPBlocks.*;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import software.bernie.techarium.integration.Integration;
import software.bernie.techarium.util.ChancedItemStack;
import software.bernie.techarium.util.ChancedItemStackList;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BiomesOPlentyIntegration extends Integration {
    public BiomesOPlentyIntegration(String modID) {
        super(modID);
    }

    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        Map<Block, Block> saplings = new HashMap<>();
        saplings.put(cherry_log, white_cherry_sapling);
        saplings.put(cherry_log, pink_cherry_sapling);
        saplings.put(dead_log, dead_sapling);
        saplings.put(fir_log, fir_sapling);
        saplings.put(hellbark_log, hellbark_sapling);
        saplings.put(jacaranda_log, jacaranda_sapling);
        saplings.put(magic_log, magic_sapling);
        saplings.put(mahogany_log, mahogany_sapling);
        saplings.put(palm_log, palm_sapling);
        saplings.put(redwood_log, redwood_sapling);
        saplings.put(umbran_log, umbran_sapling);
        saplings.put(willow_log, willow_sapling);
        for (Map.Entry<Block, Block> entry : saplings.entrySet()) {
            buildArboretumRecipe(entry.getValue().asItem(), ChancedItemStackList.of(ChancedItemStack.of(entry.getKey().asItem(), 6, 0.5), ChancedItemStack.of(entry.getValue().asItem())), 1000, 1000, consumer);
        }
    }
}
