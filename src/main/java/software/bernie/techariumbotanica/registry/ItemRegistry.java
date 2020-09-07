package software.bernie.techariumbotanica.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.item.ItemBotarium;
import software.bernie.techariumbotanica.item.PowerStickDebug;

import java.util.ArrayList;
import java.util.List;


public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TechariumBotanica.ModID);

	public static final RegistryObject<PowerStickDebug> DEBUGSTICK = ITEMS.register("power_stick",() -> new PowerStickDebug());

	public static void register(IEventBus bus){
		ITEMS.register(bus);
	}

}
