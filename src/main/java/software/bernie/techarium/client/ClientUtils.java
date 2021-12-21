package software.bernie.techarium.client;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.client.tile.render.*;
import software.bernie.techarium.registry.BlockRegistry;

@UtilityClass
@Mod.EventBusSubscriber(modid = Techarium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientUtils {
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		registerTileRenderers();
	}

	public static void registerTileRenderers() {
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.BOTARIUM.getTileEntityType(), BotariumRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.ARBORETUM.getTileEntityType(), ArboretumRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.EXCHANGE_STATION.getTileEntityType(), ExchangeStationRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.GRAVMAGNET.getTileEntityType(), GravMagnetRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.MAGNETIC_COIL.getTileEntityType(), MagneticCoilRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockRegistry.DEPOT.getTileEntityType(), DepotRenderer::new);
	}

	@SubscribeEvent
	public static void modelBakeEvent(ModelBakeEvent event) {
		//TODO: Add Pipe ModelBaking
	}

	public static boolean isShift() {
		return InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340)
			|| InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344);
	}

	public static boolean isAdvancedItem() {
		return Minecraft.getInstance().options.advancedItemTooltips;
	}
}
