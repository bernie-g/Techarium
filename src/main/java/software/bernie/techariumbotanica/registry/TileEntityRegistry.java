package software.bernie.techariumbotanica.registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.tile.TileBotarium;
import software.bernie.techariumbotanica.tile.TileWorkbench;

public class TileEntityRegistry
{
	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
			TechariumBotanica.ModID);

	public static final RegistryObject<TileEntityType<?>> BOTARIUM_TILE = TILES.register("botarium_tile", () -> TileEntityType.Builder.create(
			() -> new TileBotarium(), BlockRegistry.BOTARIUM_TIER_1.get(), BlockRegistry.BOTARIUM_TIER_2.get(), BlockRegistry.BOTARIUM_TIER_3.get(), BlockRegistry.BOTARIUM_TIER_4.get(), BlockRegistry.BOTARIUM_TIER_5.get()).build(null));

	public static final RegistryObject<TileEntityType<?>> ARBORATORIUM_TILE = TILES.register("arboratorium_tile", () -> TileEntityType.Builder.create(
			() -> new TileBotarium(), BlockRegistry.ARBORATORIUM.get()).build(null));

	public static final RegistryObject<TileEntityType<TileWorkbench>> WORKBENCH_TILE = TILES.register("workbench_tile", () -> TileEntityType.Builder.create(
			() -> new TileWorkbench(), BlockRegistry.WORKBENCH.get()).build(null));


}
