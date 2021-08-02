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
        saplings.put(white_cherry_sapling, cherry_log);
        saplings.put(pink_cherry_sapling, cherry_log);
        saplings.put(dead_sapling, dead_log);
        saplings.put(fir_sapling, fir_log);
        saplings.put(hellbark_sapling, hellbark_log);
        saplings.put(jacaranda_sapling, jacaranda_log);
        saplings.put(magic_sapling, magic_log);
        saplings.put(mahogany_sapling, mahogany_log);
        saplings.put(palm_sapling, palm_log);
        saplings.put(redwood_sapling, redwood_log);
        saplings.put(umbran_sapling, umbran_log);
        saplings.put(willow_sapling, willow_log);
        for (Map.Entry<Block, Block> entry : saplings.entrySet()) {
            buildArboretumRecipe(entry.getKey().asItem(), ChancedItemStackList.of(ChancedItemStack.of(entry.getValue().asItem(), 6, 0.5), ChancedItemStack.of(entry.getKey().asItem())), 1000, 1000, consumer);
        }
    }
}
