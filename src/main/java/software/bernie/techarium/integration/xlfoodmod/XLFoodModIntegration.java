package software.bernie.techarium.integration.xlfoodmod;

import net.minecraft.data.IFinishedRecipe;
import onelemonyboi.xlfoodmod.init.ItemList;
import software.bernie.techarium.integration.Integration;

import java.util.function.Consumer;

public class XLFoodModIntegration extends Integration {
    public XLFoodModIntegration(String modID) {
        super(modID);
    }

    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(ItemList.STRAWBERRY_SEEDS, ItemList.STRAWBERRY, 1000, 800, consumer);
        buildBotariumRecipe(ItemList.CORN_SEEDS, ItemList.RAW_CORN, 1000, 800, consumer);
        buildBotariumRecipe(ItemList.CUCUMBER_SEEDS, ItemList.CUCUMBER, 1000, 800, consumer);
        buildBotariumRecipe(ItemList.LEMON_SEEDS, ItemList.LEMON, 1000, 800, consumer);
        buildBotariumRecipe(ItemList.LETTUCE_SEEDS, ItemList.LETTUCE, 1000, 800, consumer);
        buildBotariumRecipe(ItemList.PEPPER_SEEDS, ItemList.PEPPER, 1000, 800, consumer);
        buildBotariumRecipe(ItemList.PINEAPPLE_SEEDS, ItemList.PINEAPPLE, 1000, 800, consumer);
        buildBotariumRecipe(ItemList.TOMATO_SEEDS, ItemList.TOMATO, 1000, 800, consumer);
    }
}
