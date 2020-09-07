package software.bernie.techarium.registry;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.item.PowerStickDebug;


public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techarium.ModID);

	public static final RegistryObject<PowerStickDebug> DEBUGSTICK = ITEMS.register("pipe_stick", PowerStickDebug::new);

	public static void register(IEventBus bus){
		ITEMS.register(bus);
	}

}
