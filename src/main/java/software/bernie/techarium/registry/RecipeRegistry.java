package software.bernie.techarium.registry;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.recipe.recipe.*;
import software.bernie.techarium.recipe.serializer.*;

import static software.bernie.techarium.Techarium.MOD_ID;

public class RecipeRegistry {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER_REGISTRY = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static final RegistryObject<IRecipeSerializer<BotariumRecipe>> BOTARIUM_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("botarium", BotariumRecipeSerializer::new);
    public static final RegistryObject<IRecipeSerializer<ArboretumRecipe>> ARBORETUM_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("arboretum", ArboretumRecipeSerializer::new);
    
    public static final RegistryObject<IRecipeSerializer<GravMagnetRecipe>> GRAVMAGNET_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("gravmagnet", GravMagnetRecipeSerializer::new);

    public static final RegistryObject<IRecipeSerializer<HammerRecipe>> HAMMER_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("hammering", HammerRecipeSerializer::new);

    public static final RegistryObject<IRecipeSerializer<ExchangeStationRecipe>> EXCHANGE_STATION_SERIALIZER =
            RECIPE_SERIALIZER_REGISTRY.register("exchange_station", ExchangeStationSerializer::new);

    public static IRecipeType<BotariumRecipe> BOTARIUM_RECIPE_TYPE = IRecipeType.register("techarium:botarium");
    public static IRecipeType<ArboretumRecipe> ARBORETUM_RECIPE_TYPE = IRecipeType.register("techarium:arboretum");
    public static IRecipeType<GravMagnetRecipe> GRAVMAGNET_RECIPE_TYPE = IRecipeType.register("techarium:gravmagnet");
    public static IRecipeType<HammerRecipe> HAMMER_RECIPE_TYPE = IRecipeType.register("techarium:hammering");

    public static IRecipeType<ExchangeStationRecipe> EXCHANGE_STATION_RECIPE_TYPE = IRecipeType.register("techarium:exchange_station");

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZER_REGISTRY.register(bus);
    }

}
