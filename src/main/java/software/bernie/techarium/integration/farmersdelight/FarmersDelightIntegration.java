package software.bernie.techarium.integration.farmersdelight;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import software.bernie.techarium.integration.Integration;
import vectorwing.farmersdelight.registry.ModItems;

import java.util.function.Consumer;

public class FarmersDelightIntegration extends Integration {
    public FarmersDelightIntegration(String modID) {
        super(modID);
    }

    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(ModItems.WILD_CABBAGES.get(), Ingredient.of(ModItems.CABBAGE.get()), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.WILD_ONIONS.get(), Ingredient.of(ModItems.ONION.get()), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.ONION.get(), Ingredient.of(ModItems.ONION.get()), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.WILD_CARROTS.get(), Ingredient.of(Items.CARROT), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.WILD_POTATOES.get(), Ingredient.of(Items.POTATO), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.WILD_RICE.get(), Ingredient.of(ModItems.RICE_PANICLE.get()), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.RICE.get(), Ingredient.of(ModItems.RICE_PANICLE.get()), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.WILD_TOMATOES.get(), Ingredient.of(ModItems.TOMATO.get()), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.TOMATO_SEEDS.get(), Ingredient.of(ModItems.TOMATO.get()), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.WILD_BEETROOTS.get(), Ingredient.of(Items.BEETROOT), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.CABBAGE_SEEDS.get(), Ingredient.of(ModItems.CABBAGE.get()), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.BROWN_MUSHROOM_COLONY.get(), Ingredient.of(Items.BROWN_MUSHROOM), Ingredient.of(Items.STONE, Items.MYCELIUM), 1000, 800, consumer);
        buildBotariumRecipe(ModItems.RED_MUSHROOM_COLONY.get(), Ingredient.of(Items.RED_MUSHROOM), Ingredient.of(Items.STONE, Items.MYCELIUM), 1000, 800, consumer);
    }
}
