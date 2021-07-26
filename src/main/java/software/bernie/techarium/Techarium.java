package software.bernie.techarium;

import static software.bernie.techarium.registry.ContainerRegistry.ASSEMBLER_CONTAINER;
import static software.bernie.techarium.registry.ContainerRegistry.AUTO_CONTAINER;
import static software.bernie.techarium.registry.ContainerRegistry.EXCHANGE_STATION_CONTAINER;
import static software.bernie.techarium.registry.ContainerRegistry.PIPE_CONTAINER;

import org.apache.logging.log4j.Logger;

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
import software.bernie.geckolib3.GeckoLib;
import software.bernie.techarium.datagen.TechariumBlockStateProvider;
import software.bernie.techarium.datagen.TechariumBlockTagsProvider;
import software.bernie.techarium.datagen.TechariumItemModelProvider;
import software.bernie.techarium.datagen.TechariumItemTagsProvider;
import software.bernie.techarium.datagen.TechariumLangProvider;
import software.bernie.techarium.datagen.TechariumLootTableProvider;
import software.bernie.techarium.datagen.TechariumRecipeProvider;
import software.bernie.techarium.display.screen.AssemblerContainerScreen;
import software.bernie.techarium.display.screen.AutomaticContainerScreen;
import software.bernie.techarium.display.screen.ExchangeStationScreen;
import software.bernie.techarium.display.screen.PipeContainerScreen;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.integration.theoneprobe.TheOneProbeIntegration;
import software.bernie.techarium.network.NetworkConnection;
import software.bernie.techarium.pipe.NetworkEvents;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.ContainerRegistry;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.util.LogCache;
import software.bernie.techarium.world.WorldGen;

@Mod(Techarium.ModID)
public class Techarium {
	public final static String ModID = "techarium";
	public static Logger LOGGER;

	public Techarium() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		GeckoLib.initialize();
		LOGGER = LogCache.getLogger(getClass());
		ItemRegistry.register(bus);
		BlockRegistry.register(bus);
		ContainerRegistry.register(bus);
		RecipeRegistry.register(bus);
		bus.addListener(this::onClientSetup);
		bus.addListener(NetworkEvents::onCommonSetup);
		bus.addListener(this::gatherData);
		bus.addListener(this::enqueueIMC);
		MinecraftForge.EVENT_BUS.addListener(WorldGen::generateOres);

		NetworkConnection.registerMessages();
	}

	public static ResourceLocation rl(String path) {
		return new ResourceLocation(Techarium.ModID, path);
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
		ScreenManager.register(ASSEMBLER_CONTAINER.get(), AssemblerContainerScreen::new);
	}
}
