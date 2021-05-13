package software.bernie.techarium.registry;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.recipes.serializer.BotariumRecipeSerializer;

import static software.bernie.techarium.Techarium.ModID;

public class RecipeRegistry {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER_REGISTRY = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, ModID);

    public static final RegistryObject<IRecipeSerializer<BotariumRecipe>> BOTARIUM_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("botarium", BotariumRecipeSerializer::new);

    public static IRecipeType<BotariumRecipe> BOTARIUM_RECIPE_TYPE = IRecipeType.register("techarium:botarium");

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZER_REGISTRY.register(bus);
    }

}
