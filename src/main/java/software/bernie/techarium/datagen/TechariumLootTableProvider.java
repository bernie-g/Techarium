package software.bernie.techarium.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import software.bernie.techarium.registry.BlockRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TechariumLootTableProvider extends LootTableProvider {
	public TechariumLootTableProvider(DataGenerator dataGenerator) {
		super(dataGenerator);
	}


	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
		return ImmutableList.of(Pair.of(Blocks::new, LootParameterSets.BLOCK));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationTracker) {
		map.forEach((p_218436_2_, p_218436_3_) -> LootTableManager.validate(validationTracker, p_218436_2_, p_218436_3_));
	}

	public static class Blocks extends TechariumBlockLootTableProvider {
		@Override
		protected void addTables() {
			dropSelf(BlockRegistry.ALUMINIUM_ORE);
			dropSelf(BlockRegistry.COPPER_ORE);
			dropSelf(BlockRegistry.LEAD_ORE);
			dropSelf(BlockRegistry.NICKEL_ORE);

			dropSelf(BlockRegistry.ALUMINIUM_BLOCK);
			dropSelf(BlockRegistry.COPPER_BLOCK);
			dropSelf(BlockRegistry.LEAD_BLOCK);
			dropSelf(BlockRegistry.NICKEL_BLOCK);

			dropSelf(BlockRegistry.ARBORETUM);
			dropSelf(BlockRegistry.BOTARIUM);
			dropSelf(BlockRegistry.GRAVMAGNET);
			dropSelf(BlockRegistry.EXCHANGE_STATION);
			dropSelf(BlockRegistry.PIPE);
			dropSelf(BlockRegistry.MAGNETIC_COIL);
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return BlockRegistry.BLOCKS.getEntries().stream()
					.map(RegistryObject::get)
					.collect(Collectors.toList());
		}
	}
}
