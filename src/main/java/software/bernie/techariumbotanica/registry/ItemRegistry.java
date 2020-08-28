package software.bernie.techariumbotanica.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.item.ItemBotarium;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TechariumBotanica.ModID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry
{
	public static final DeferredRegister ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TechariumBotanica.ModID);

	public static final RegistryObject<BlockItem> BOTARIUM_TIER_1 = ITEMS.register("botarium_tier_1", () -> new ItemBotarium(BlockRegistry.BOTARIUM_TIER_1.get(), new Item.Properties().group(ItemGroupRegistry.TECHARIUMS)));
	public static final RegistryObject<BlockItem> BOTARIUM_TIER_2 = ITEMS.register("botarium_tier_2", () -> new ItemBotarium(BlockRegistry.BOTARIUM_TIER_2.get(), new Item.Properties().group(ItemGroupRegistry.TECHARIUMS)));
	public static final RegistryObject<BlockItem> BOTARIUM_TIER_3 = ITEMS.register("botarium_tier_3", () -> new ItemBotarium(BlockRegistry.BOTARIUM_TIER_3.get(), new Item.Properties().group(ItemGroupRegistry.TECHARIUMS)));
	public static final RegistryObject<BlockItem> BOTARIUM_TIER_4 = ITEMS.register("botarium_tier_4", () -> new ItemBotarium(BlockRegistry.BOTARIUM_TIER_4.get(), new Item.Properties().group(ItemGroupRegistry.TECHARIUMS)));
	public static final RegistryObject<BlockItem> BOTARIUM_TIER_5 = ITEMS.register("botarium_tier_5", () -> new ItemBotarium(BlockRegistry.BOTARIUM_TIER_5.get(), new Item.Properties().group(ItemGroupRegistry.TECHARIUMS)));

	public static final RegistryObject<BlockItem> ARBORATORIUM = ITEMS.register("arboratorium", () -> new ItemBotarium(BlockRegistry.ARBORATORIUM.get(), new Item.Properties().group(ItemGroupRegistry.TECHARIUMS)));


	public static List<RegistryObject<Block>> NormalItemBlocks = new ArrayList<>();

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();

		for(RegistryObject<Block> block : NormalItemBlocks)
		{
			ResourceLocation registryName = block.get().getRegistryName();
			registry.register(new BlockItem(block.get(), new Item.Properties().group(ItemGroupRegistry.TECHARIUMS)).setRegistryName(
					registryName));
		}
	}
}
