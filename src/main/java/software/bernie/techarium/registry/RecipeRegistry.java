package software.bernie.techarium.registry;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.client.tile.render.GravMagnetRenderer;
import software.bernie.techarium.recipe.recipe.ArboretumRecipe;
import software.bernie.techarium.recipe.recipe.BotariumRecipe;
import software.bernie.techarium.recipe.recipe.ExchangeStationRecipe;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;
import software.bernie.techarium.recipe.serializer.ArboretumRecipeSerializer;
import software.bernie.techarium.recipe.serializer.BotariumRecipeSerializer;
import software.bernie.techarium.recipe.serializer.ExchangeStationSerializer;
import software.bernie.techarium.recipe.serializer.GravMagnetRecipeSerializer;

import static software.bernie.techarium.Techarium.ModID;

public class RecipeRegistry {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER_REGISTRY = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, ModID);

    public static final RegistryObject<IRecipeSerializer<BotariumRecipe>> BOTARIUM_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("botarium", BotariumRecipeSerializer::new);
    public static final RegistryObject<IRecipeSerializer<ArboretumRecipe>> ARBORETUM_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("arboretum", ArboretumRecipeSerializer::new);
    
    public static final RegistryObject<IRecipeSerializer<GravMagnetRecipe>> GRAVMAGNET_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("gravmagnet", GravMagnetRecipeSerializer::new);

    public static final RegistryObject<IRecipeSerializer<ExchangeStationRecipe>> EXCHANGE_STATION_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("exchange_station", ExchangeStationSerializer::new);

    public static IRecipeType<BotariumRecipe> BOTARIUM_RECIPE_TYPE = IRecipeType.register("techarium:botarium");
    public static IRecipeType<ArboretumRecipe> ARBORETUM_RECIPE_TYPE = IRecipeType.register("techarium:arboretum");
    public static IRecipeType<GravMagnetRecipe> GRAVMAGNET_RECIPE_TYPE = IRecipeType.register("techarium:gravmagnet");

    public static IRecipeType<ExchangeStationRecipe> EXCHANGE_STATION_RECIPE_TYPE = IRecipeType.register("techarium:exchange_station");

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZER_REGISTRY.register(bus);
    }

}
