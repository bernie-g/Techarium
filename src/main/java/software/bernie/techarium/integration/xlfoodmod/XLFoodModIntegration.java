package software.bernie.techarium.integration.xlfoodmod;

import net.minecraft.data.IFinishedRecipe;
import onelemonyboi.xlfoodmod.init.ItemList;
import software.bernie.techarium.integration.Integration;
import software.bernie.techarium.util.ChancedItemStackList;

import java.util.function.Consumer;

public class XLFoodModIntegration extends Integration {
    public XLFoodModIntegration(String modID) {
        super(modID);
    }

    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        buildBotariumFarmlandRecipe(ItemList.STRAWBERRY_SEEDS, ChancedItemStackList.of(ItemList.STRAWBERRY), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(ItemList.CORN_SEEDS, ChancedItemStackList.of(ItemList.RAW_CORN), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(ItemList.CUCUMBER_SEEDS, ChancedItemStackList.of(ItemList.CUCUMBER), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(ItemList.LEMON_SEEDS, ChancedItemStackList.of(ItemList.LEMON), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(ItemList.LETTUCE_SEEDS, ChancedItemStackList.of(ItemList.LETTUCE), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(ItemList.PEPPER_SEEDS, ChancedItemStackList.of(ItemList.PEPPER), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(ItemList.PINEAPPLE_SEEDS, ChancedItemStackList.of(ItemList.PINEAPPLE), 1000, 800, consumer);
        buildBotariumFarmlandRecipe(ItemList.TOMATO_SEEDS, ChancedItemStackList.of(ItemList.TOMATO), 1000, 800, consumer);
    }
}
