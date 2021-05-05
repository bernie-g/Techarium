package software.bernie.techarium.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

		}

	}
}
