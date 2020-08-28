package software.bernie.techariumbotanica.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.ValidationTracker;
import software.bernie.techariumbotanica.registry.BlockRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TechariumLootTables extends LootTableProvider
{
	public TechariumLootTables(DataGenerator dataGenerator)
	{
		super(dataGenerator);
	}

	@Override
	public String getName()
	{
		return "Techarium Botanica LootTables";
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
	{
		return ImmutableList.of(Pair.of(Blocks::new, LootParameterSets.BLOCK));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationresults)
	{
	}

	public static class Blocks extends TechariumBlockLootTableProvider
	{
		@Override
		protected void addTables()
		{
			dropSelf(BlockRegistry.BOTARIUM_TIER_1);
			dropSelf(BlockRegistry.BOTARIUM_TIER_2);
			dropSelf(BlockRegistry.BOTARIUM_TIER_3);
			dropSelf(BlockRegistry.BOTARIUM_TIER_4);
			dropSelf(BlockRegistry.BOTARIUM_TIER_5);
			dropSelf(BlockRegistry.ARBORATORIUM);

		}

		@Override
		protected Iterable<Block> getKnownBlocks()
		{
			return BlockRegistry.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
		}
	}
}
