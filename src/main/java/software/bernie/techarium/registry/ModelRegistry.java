package software.bernie.techarium.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.techarium.Techarium;

@Mod.EventBusSubscriber(modid = Techarium.ModID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelRegistry
{
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		ModelLoader.addSpecialModel(new ResourceLocation(Techarium.ModID, "block/botarium_tier_1"));
		ModelLoader.addSpecialModel(new ResourceLocation(Techarium.ModID, "block/botarium_tier_2"));
		ModelLoader.addSpecialModel(new ResourceLocation(Techarium.ModID, "block/botarium_tier_3"));
		ModelLoader.addSpecialModel(new ResourceLocation(Techarium.ModID, "block/botarium_tier_4"));
		ModelLoader.addSpecialModel(new ResourceLocation(Techarium.ModID, "block/botarium_tier_5"));
		ModelLoader.addSpecialModel(new ResourceLocation(Techarium.ModID, "block/arboratorium"));

	}
}
