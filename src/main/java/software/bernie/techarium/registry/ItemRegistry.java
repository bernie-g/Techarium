package software.bernie.techarium.registry;

import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.item.PipeItem;
import software.bernie.techarium.item.PowerStickDebug;
import software.bernie.techarium.pipe.util.PipeType;

import java.util.function.Supplier;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUM;


public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techarium.ModID);
	@Getter
	public static final RegistryObject<PowerStickDebug> DEBUGSTICK = ITEMS.register("pipe_stick", PowerStickDebug::new);
	public static final RegistryObject<PipeItem> ITEM_PIPE = ITEMS.register("item_pipe", () -> new PipeItem(PipeType.ITEM));
	public static final RegistryObject<PipeItem> FLUID_PIPE = ITEMS.register("fluid_pipe", () -> new PipeItem(PipeType.FLUID));
	public static final RegistryObject<PipeItem> ENERGY_PIPE = ITEMS.register("energy_pipe", () -> new PipeItem(PipeType.ENERGY))
			;
	public static final RegistryObject<Item> ALUMINIUM_INGOT = ITEMS.register("aluminium_ingot", itemCreator());
	public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", itemCreator());
	public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot", itemCreator());
	public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot", itemCreator());

	public static final RegistryObject<Item> ALUMINIUM_NUGGET = ITEMS.register("aluminium_nugget", itemCreator());
	public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", itemCreator());
	public static final RegistryObject<Item> LEAD_NUGGET = ITEMS.register("lead_nugget", itemCreator());
	public static final RegistryObject<Item> NICKEL_NUGGET = ITEMS.register("nickel_nugget", itemCreator());

	public static Supplier<Item> itemCreator() {
		return () -> new Item(new Item.Properties().tab(TECHARIUM));
	}

	public static void register(IEventBus bus){
		ITEMS.register(bus);
	}
}
