package software.bernie.techarium.integration.pams;

import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import pam.pamhc2crops.items.ItemPamSeed;
import software.bernie.techarium.integration.Integration;
import software.bernie.techarium.util.ChancedItemStackList;

import java.util.function.Consumer;

public class PamsHarvestCraftIntegration extends Integration {
    public PamsHarvestCraftIntegration(String modID) {
        super(modID);
    }

    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof ItemPamSeed) {
                ItemPamSeed seed = (ItemPamSeed) item;
                Block block = seed.getBlock();
                Item drop = getPamsDropFromCrop(block);
                buildBotariumFarmlandRecipe(seed,ChancedItemStackList.of(drop), 1000, 1000, consumer);
            }
        }
    }

    private Item getPamsDropFromCrop(Block block) {
        if (block.asItem() == Items.AIR)
            throw new IllegalStateException("Could not find Pams Drop Item of Drop Block: " + block);
        return block.asItem();
    }
}
