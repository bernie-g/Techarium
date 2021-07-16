package software.bernie.techarium.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarrotBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.fml.RegistryObject;
import software.bernie.techarium.block.voltaicpile.Charge;
import software.bernie.techarium.block.voltaicpile.VoltaicPileBlock;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.ItemRegistry;

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
			dropSelf(BlockRegistry.ZINC_ORE);

			dropSelf(BlockRegistry.ALUMINIUM_BLOCK);
			dropSelf(BlockRegistry.COPPER_BLOCK);
			dropSelf(BlockRegistry.LEAD_BLOCK);
			dropSelf(BlockRegistry.NICKEL_BLOCK);
			dropSelf(BlockRegistry.ZINC_BLOCK);

			noDrop(BlockRegistry.ARBORETUM);
			noDrop(BlockRegistry.BOTARIUM);
			noDrop(BlockRegistry.EXCHANGE_STATION);
			noDrop(BlockRegistry.VOLTAIC_PILE);
			dropSelf(BlockRegistry.PIPE);

			ILootCondition.IBuilder notEmpty = BlockStateProperty.hasBlockStateProperties(BlockRegistry.VOLTAIC_PILE.get())
					.setProperties(StatePropertiesPredicate.Builder.properties()
							.hasProperty(VoltaicPileBlock.CHARGE, Charge.ONE_THIRD)
							.hasProperty(VoltaicPileBlock.CHARGE, Charge.TWO_THIRD)
							.hasProperty(VoltaicPileBlock.CHARGE, Charge.FULL));

			ILootCondition.IBuilder empty = BlockStateProperty.hasBlockStateProperties(BlockRegistry.VOLTAIC_PILE.get())
					.setProperties(StatePropertiesPredicate.Builder.properties()
							.hasProperty(VoltaicPileBlock.CHARGE, Charge.EMPTY));

			customBlockLootTable(BlockRegistry.VOLTAIC_PILE.get(),
					ItemLootEntry.lootTableItem(BlockRegistry.VOLTAIC_PILE.get())
							.when(notEmpty)
							.apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY)
									.copy("energy", "BlockEntityTag.energy")),
					ItemLootEntry.lootTableItem(ItemRegistry.COPPER_INGOT.get())
							.when(empty),
					ItemLootEntry.lootTableItem(ItemRegistry.ZINC_INGOT.get())
							.when(empty));
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return BlockRegistry.BLOCKS.getEntries().stream()
					.map(RegistryObject::get)
					.collect(Collectors.toList());
		}
	}
}
