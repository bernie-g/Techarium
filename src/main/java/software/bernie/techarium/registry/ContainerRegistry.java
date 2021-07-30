package software.bernie.techarium.registry;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.display.container.ExchangeStationContainer;
import software.bernie.techarium.display.container.PipeContainer;

public class ContainerRegistry
{
	public static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Techarium.MOD_ID);

	public static RegistryObject<ContainerType<AutomaticContainer>> AUTO_CONTAINER = CONTAINERS.register("auto_bot", () -> IForgeContainerType.create(AutomaticContainer::new));
	public static RegistryObject<ContainerType<PipeContainer>> PIPE_CONTAINER = CONTAINERS.register("pipe_container", () -> IForgeContainerType.create(PipeContainer::new));
	public static RegistryObject<ContainerType<ExchangeStationContainer>> EXCHANGE_STATION_CONTAINER = CONTAINERS.register("exchange_station", () -> IForgeContainerType.create(ExchangeStationContainer::new));

	public static void register(IEventBus bus){
		CONTAINERS.register(bus);
	}
}
