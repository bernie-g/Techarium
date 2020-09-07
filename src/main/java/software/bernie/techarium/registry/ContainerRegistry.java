package software.bernie.techariumbotanica.registry;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.machine.container.AutomaticContainer;

public class ContainerRegistry
{
	public static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, TechariumBotanica.ModID);

	public static RegistryObject<ContainerType<AutomaticContainer>> AUTO_CONTAINER = CONTAINERS.register("auto_bot", () -> IForgeContainerType.create(AutomaticContainer::new));

	public static void register(IEventBus bus){
		CONTAINERS.register(bus);
	}
}
