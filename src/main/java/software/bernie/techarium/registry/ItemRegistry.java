package software.bernie.techarium.registry;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.item.PipeItem;
import software.bernie.techarium.item.PowerStickDebug;
import software.bernie.techarium.pipes.capability.PipeType;


public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techarium.ModID);

	public static final RegistryObject<PowerStickDebug> DEBUGSTICK = ITEMS.register("pipe_stick", PowerStickDebug::new);
	public static final RegistryObject<PipeItem> ITEM_PIPE = ITEMS.register("item_pipe", () -> new PipeItem(PipeType.ITEM));
	public static final RegistryObject<PipeItem> FLUID_PIPE = ITEMS.register("fluid_pipe", () -> new PipeItem(PipeType.FLUID));
	public static final RegistryObject<PipeItem> ENERGY_PIPE = ITEMS.register("energy_pipe", () -> new PipeItem(PipeType.ENERGY));

	public static void register(IEventBus bus){
		ITEMS.register(bus);
	}

}
