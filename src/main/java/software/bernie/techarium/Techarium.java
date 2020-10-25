package software.bernie.techarium;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.resource.ResourceListener;
import software.bernie.techarium.datagen.TechariumLootTables;
import software.bernie.techarium.machine.screen.AutomaticContainerScreen;
import software.bernie.techarium.registry.*;

import static software.bernie.techarium.registry.ContainerRegistry.AUTO_CONTAINER;

@Mod(Techarium.ModID)
public class Techarium
{
	public final static String ModID = "techarium";
	public static Logger LOGGER;

	public Techarium()
	{
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ResourceListener::registerReloadListener);

        LOGGER = LogManager.getLogger();
		ItemRegistry.register(bus);
		BlockTileRegistry.register(bus);
		ContainerRegistry.register(bus);
		CropTypeRegistry.register(bus);
		RecipeSerializerRegistry.register(bus);
		bus.addListener(this::onClientSetup);
		bus.addListener(this::gatherData);
	}

	private void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		generator.addProvider(new TechariumLootTables(generator));
	}

	public void onClientSetup(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(AUTO_CONTAINER.get(), AutomaticContainerScreen::new);
	}
}
