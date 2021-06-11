package software.bernie.techarium.integration.xlfoodmod;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.Ingredient;
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
        buildBotariumRecipe(ItemList.STRAWBERRY_SEEDS, ChancedItemStackList.of(ItemList.STRAWBERRY), 1000, 800, consumer);
        buildBotariumRecipe(ItemList.CORN_SEEDS, ChancedItemStackList.of(ItemList.RAW_CORN), 1000, 800, consumer);
        buildBotariumRecipe(ItemList.CUCUMBER_SEEDS, ChancedItemStackList.of(ItemList.CUCUMBER), 1000, 800, consumer);
        buildBotariumRecipe(ItemList.LEMON_SEEDS, ChancedItemStackList.of(ItemList.LEMON), 1000, 800, consumer);
        buildBotariumRecipe(ItemList.LETTUCE_SEEDS, ChancedItemStackList.of(ItemList.LETTUCE), 1000, 800, consumer);
        buildBotariumRecipe(ItemList.PEPPER_SEEDS, ChancedItemStackList.of(ItemList.PEPPER), 1000, 800, consumer);
        buildBotariumRecipe(ItemList.PINEAPPLE_SEEDS, ChancedItemStackList.of(ItemList.PINEAPPLE), 1000, 800, consumer);
        buildBotariumRecipe(ItemList.TOMATO_SEEDS, ChancedItemStackList.of(ItemList.TOMATO), 1000, 800, consumer);
    }
}
