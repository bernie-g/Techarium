package software.bernie.techariumbotanica.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib.animation.model.AnimatedEntityModel;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.client.render.tile.RenderBotarium;
import software.bernie.techariumbotanica.registry.BlockRegistry;
import software.bernie.techariumbotanica.registry.TileEntityRegistry;
import software.bernie.geckolib.animation.controller.*;
@Mod.EventBusSubscriber(modid = TechariumBotanica.ModID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents
{
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(BlockRegistry.BOTARIUM_TIER_1.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockRegistry.BOTARIUM_TIER_2.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockRegistry.BOTARIUM_TIER_3.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockRegistry.BOTARIUM_TIER_4.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockRegistry.BOTARIUM_TIER_5.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockRegistry.ARBORATORIUM.get(), RenderType.getTranslucent());

		registerTileRenderers();
	}

	public static void registerTileRenderers()
	{
		ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.BOTARIUM_TILE.get(), RenderBotarium::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.ARBORATORIUM_TILE.get(), RenderBotarium::new);

	}
}
