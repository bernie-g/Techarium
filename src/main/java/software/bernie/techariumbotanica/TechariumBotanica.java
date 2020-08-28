package software.bernie.techariumbotanica;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.animation.controller.AnimationController;
import software.bernie.geckolib.animation.render.AnimatedModelRenderer;
import software.bernie.geckolib.util.AnimationUtils;
import software.bernie.techariumbotanica.client.render.tile.WorkbenchTESR;
import software.bernie.techariumbotanica.client.screen.BotariumScreen;
import software.bernie.techariumbotanica.datagen.TechariumLootTables;
import software.bernie.techariumbotanica.registry.*;

@Mod(TechariumBotanica.ModID)
public class TechariumBotanica
{
	public final static String ModID = "techarium-botanica";
	public static Logger LOGGER;

	public TechariumBotanica()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		LOGGER = LogManager.getLogger();

		BlockRegistry.BLOCKS.register(bus);
		ItemRegistry.ITEMS.register(bus);
		TileEntityRegistry.TILES.register(bus);
		ContainerRegistry.CONTAINERS.register(bus);
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
		ScreenManager.registerFactory(ContainerRegistry.BOTARIUM_1.get(), BotariumScreen::new);

		ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.WORKBENCH_TILE.get(), WorkbenchTESR::new);
		RenderTypeLookup.setRenderLayer(BlockRegistry.WORKBENCH.get(), RenderType.getCutout());

	}
}
