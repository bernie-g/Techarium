package software.bernie.techarium.client;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.techarium.Techarium;

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


	}
}
