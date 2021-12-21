package software.bernie.techarium.registry;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUM;

import java.util.function.Supplier;

import lombok.Getter;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.coils.MagneticCoilType;
import software.bernie.techarium.item.PipeItem;
import software.bernie.techarium.item.PowerStickDebug;
import software.bernie.techarium.item.CoilItem;
import software.bernie.techarium.pipe.util.PipeType;


public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techarium.MOD_ID);
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
	public static final RegistryObject<Item> ZINC_INGOT = ITEMS.register("zinc_ingot", itemCreator());

	public static final RegistryObject<Item> ALUMINIUM_NUGGET = ITEMS.register("aluminium_nugget", itemCreator());
	public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", itemCreator());
	public static final RegistryObject<Item> LEAD_NUGGET = ITEMS.register("lead_nugget", itemCreator());
	public static final RegistryObject<Item> NICKEL_NUGGET = ITEMS.register("nickel_nugget", itemCreator());
	public static final RegistryObject<Item> ZINC_NUGGET = ITEMS.register("zinc_nugget", itemCreator());

	public static final RegistryObject<Item> ALUMINIUM_PLATE = ITEMS.register("aluminium_plate", itemCreator());
	public static final RegistryObject<Item> COPPER_PLATE = ITEMS.register("copper_plate", itemCreator());
	public static final RegistryObject<Item> LEAD_PLATE = ITEMS.register("lead_plate", itemCreator());
	public static final RegistryObject<Item> NICKEL_PLATE = ITEMS.register("nickel_plate", itemCreator());
	public static final RegistryObject<Item> ZINC_PLATE = ITEMS.register("zinc_plate", itemCreator());
	public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", itemCreator());

	public static final RegistryObject<Item> COPPER_COIL = ITEMS.register("copper_coil", () -> new CoilItem(MagneticCoilType.TIER_1));
	public static final RegistryObject<Item> COBALT_COIL = ITEMS.register("cobalt_coil", () -> new CoilItem(MagneticCoilType.TIER_2));
	public static final RegistryObject<Item> SOLARIUM_COIL = ITEMS.register("solarium_coil", () -> new CoilItem(MagneticCoilType.TIER_3));

	public static Supplier<Item> itemCreator() {
		return () -> new Item(new Item.Properties().tab(TECHARIUM));
	}

	public static void register(IEventBus bus){
		ITEMS.register(bus);
	}
}
