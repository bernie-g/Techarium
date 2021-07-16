package software.bernie.techarium.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Item;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.util.IItemProvider;

import java.util.function.Function;
import java.util.function.Supplier;

public class TechariumBlockLootTableProvider extends BlockLootTables
{
	public void registerTable(Supplier<? extends Block> block, Function<Block, LootTable.Builder> factory) {
		super.add(block.get(), factory);
	}

	public void dropSelf(Supplier<? extends Block> block) {
		super.dropSelf(block.get());
	}

	public void noDrop(Supplier<? extends Block> block) {
		super.add(block.get(), noDrop());
	}

	public void dropAsSilk(Supplier<? extends Block> block) {
		super.dropWhenSilkTouch(block.get());
	}

	public void dropWithSilk(Supplier<? extends Block> block, Supplier<? extends IItemProvider> drop) {
		add(block.get(), (result) -> createSingleItemTableWithSilkTouch(result, drop.get()));
	}

	public void dropWithFortune(Supplier<? extends Block> block, Supplier<? extends Item> drop) {
		super.add(block.get(), (result) -> createOreDrop(result, drop.get()));
	}

	public void customBlockLootTable(Block block, LootEntry.Builder<?>... builders) {
		LootPool.Builder builder = LootPool.lootPool();

		for (LootEntry.Builder<?> entryBuilder : builders) {
			builder = builder.add(entryBuilder);
		}

		add(block, LootTable.lootTable().withPool(applyExplosionCondition(block, builder)));
	}
}
