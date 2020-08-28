package software.bernie.techariumbotanica.registry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.block.ArboratoriumBase;
import software.bernie.techariumbotanica.block.BotariumBase;
import software.bernie.techariumbotanica.block.BotariumTop;
import software.bernie.techariumbotanica.block.WorkbenchBlock;

import java.util.HashMap;
import java.util.function.Supplier;

public class BlockRegistry
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			TechariumBotanica.ModID);

	public static final HashMap<Integer, RegistryObject<Block>> BOTARIUMS = new HashMap<>();


	public static final RegistryObject<Block> BOTARIUM_TOP = register("botarium_top", () -> new BotariumTop());

	public static final RegistryObject<Block> BOTARIUM_TIER_1 = registerBotarium(1, "botarium_tier_1", () -> new BotariumBase(1));
	public static final RegistryObject<Block> BOTARIUM_TIER_2 = registerBotarium(2, "botarium_tier_2", () -> new BotariumBase(2));
	public static final RegistryObject<Block> BOTARIUM_TIER_3 = registerBotarium(3, "botarium_tier_3", () -> new BotariumBase(3));
	public static final RegistryObject<Block> BOTARIUM_TIER_4 = registerBotarium(4, "botarium_tier_4", () -> new BotariumBase(4));
	public static final RegistryObject<Block> BOTARIUM_TIER_5 = registerBotarium(5, "botarium_tier_5", () -> new BotariumBase(5));

	public static final RegistryObject<Block> ARBORATORIUM = register("arboratorium", () -> new ArboratoriumBase());

	public static final RegistryObject<Block> WORKBENCH = register("workbench", () -> new WorkbenchBlock(Block.Properties.create(Material.PISTON)));

	private static <T extends Block> RegistryObject<T> registerBlockAndItem(String name, Supplier<? extends T> block)
	{
		RegistryObject<? extends T> registryObject = register(name, block);
		ItemRegistry.NormalItemBlocks.add((RegistryObject<Block>) registryObject);
		return (RegistryObject<T>) registryObject;
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> block) {
		RegistryObject<T> register = BLOCKS.register(name, block);
		return register;
	}

	private static <T extends Block> RegistryObject<T> registerBotarium(int tier, String name, Supplier<? extends T> block) {
		RegistryObject<? extends T> blockRegistry = register(name, block);
		BOTARIUMS.put(tier, (RegistryObject<Block>) blockRegistry);
		return (RegistryObject<T>) blockRegistry;
	}
}
