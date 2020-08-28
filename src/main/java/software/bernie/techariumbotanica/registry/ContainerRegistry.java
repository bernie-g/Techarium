package software.bernie.techariumbotanica.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.container.BotariumContainer;

import java.util.HashMap;
import java.util.function.Supplier;

public class ContainerRegistry
{
	public static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, TechariumBotanica.ModID);

	public static HashMap<Integer, RegistryObject<? extends ContainerType<?>>> BOTARIUMS = new HashMap<>();

	public static RegistryObject<ContainerType<BotariumContainer>> BOTARIUM_1 = registerBotarium(1, "botarium_1", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		BlockPos pos = data.readBlockPos();
		return new BotariumContainer(1, windowId, Minecraft.getInstance().world, pos, inv, Minecraft.getInstance().player);
	}));


	public static <T extends Container> RegistryObject<ContainerType<T>> register(String name, Supplier<ContainerType<T>> containerSupplier)
	{
		return CONTAINERS.register(name, containerSupplier);
	}

	public static <T extends Container> RegistryObject<ContainerType<T>> registerBotarium(int tier, String name, Supplier<ContainerType<T>> containerSupplier)
	{
		RegistryObject<ContainerType<T>> container = register(name, containerSupplier);
		BOTARIUMS.put(tier, container);

		return container;
	}

}
