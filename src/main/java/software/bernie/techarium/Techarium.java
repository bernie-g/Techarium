package software.bernie.techarium;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.techarium.config.TechariumConfig;
import software.bernie.techarium.datagen.*;
import software.bernie.techarium.display.screen.AutomaticContainerScreen;
import software.bernie.techarium.display.screen.ExchangeStationScreen;
import software.bernie.techarium.display.screen.PipeContainerScreen;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.integration.theoneprobe.TheOneProbeIntegration;
import software.bernie.techarium.network.NetworkConnection;
import software.bernie.techarium.pipe.NetworkEvents;
import software.bernie.techarium.registry.*;
import software.bernie.techarium.util.LogCache;
import software.bernie.techarium.world.WorldGen;

import static software.bernie.techarium.registry.ContainerRegistry.*;

@Mod(Techarium.MOD_ID)
public class Techarium {
	public static final String MOD_ID = "techarium";
	public static final Logger LOGGER = LogCache.getLogger(Techarium.class);

	public Techarium() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		GeckoLib.initialize();
		ItemRegistry.register(bus);
		BlockRegistry.register(bus);
		ContainerRegistry.register(bus);
		RecipeRegistry.register(bus);
		bus.addListener(this::onClientSetup);
		bus.addListener(NetworkEvents::onCommonSetup);
		bus.addListener(this::gatherData);
		bus.addListener(this::enqueueIMC);
		bus.addListener(LootRegistry::registerLootPoolEntryTypes);
		MinecraftForge.EVENT_BUS.addListener(WorldGen::generateOres);
		TechariumConfig.load();
		NetworkConnection.registerMessages();
	}

	public static ResourceLocation rl(String path) {
		return new ResourceLocation(Techarium.MOD_ID, path);
	}

	private void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		TechariumBlockTagsProvider provider = new TechariumBlockTagsProvider(generator, existingFileHelper);

		generator.addProvider(new TechariumRecipeProvider(generator));
		generator.addProvider(new TechariumLangProvider(generator));
		generator.addProvider(provider);
		generator.addProvider(new TechariumItemTagsProvider(generator, provider, existingFileHelper));
		generator.addProvider(new TechariumLootTableProvider(generator));
		generator.addProvider(new TechariumBlockStateProvider(generator, existingFileHelper));
		generator.addProvider(new TechariumItemModelProvider(generator, existingFileHelper));
	}

	public void enqueueIMC(InterModEnqueueEvent event) {
		ModIntegrations.getTheOneProbe().ifPresent(TheOneProbeIntegration::requestTheOneProbe);
	}

	public void onClientSetup(FMLClientSetupEvent event) {
		ScreenManager.register(AUTO_CONTAINER.get(), AutomaticContainerScreen::new);
		ScreenManager.register(PIPE_CONTAINER.get(), PipeContainerScreen::new);
		ScreenManager.register(EXCHANGE_STATION_CONTAINER.get(), ExchangeStationScreen::new);
	}
}
