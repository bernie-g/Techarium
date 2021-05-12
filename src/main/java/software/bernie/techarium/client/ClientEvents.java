package software.bernie.techarium.client;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.client.tile.render.BotariumRenderer;
import software.bernie.techarium.client.tile.render.ExchangeStationRenderer;
import software.bernie.techarium.registry.BlockTileRegistry;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;

@Mod.EventBusSubscriber(modid = Techarium.ModID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents
{
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event)
	{
		registerTileRenderers();
	}

	public static void registerTileRenderers()
	{
		ClientRegistry.bindTileEntityRenderer(BlockTileRegistry.BOTARIUM.getTileEntityType(), BotariumRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockTileRegistry.EXCHANGE_STATION.getTileEntityType(), ExchangeStationRenderer::new);
	}
}
