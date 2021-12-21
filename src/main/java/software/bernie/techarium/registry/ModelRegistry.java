package software.bernie.techarium.registry;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.techarium.Techarium;

@Mod.EventBusSubscriber(modid = Techarium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelRegistry
{
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{

	}
}
