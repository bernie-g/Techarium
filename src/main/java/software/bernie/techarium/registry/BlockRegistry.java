package software.bernie.techarium.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.BlockRegistryObjectGroup;
import software.bernie.techarium.block.arboretum.ArboretumMaster;
import software.bernie.techarium.block.arboretum.ArboretumTop;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.block.botarium.BotariumTop;
import software.bernie.techarium.block.botarium.BotariumMaster;
import software.bernie.techarium.block.exchangestation.ExchangeStationBlock;
import software.bernie.techarium.block.pipe.PipeBlock;
import software.bernie.techarium.item.MachineItem;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
import software.bernie.techarium.tile.pipe.PipeTile;
import software.bernie.techarium.tile.slaves.TopEnabledOnlySlave;

import java.util.function.Function;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUM;
@SuppressWarnings("unused")
public class BlockRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
            Techarium.ModID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Techarium.ModID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techarium.ModID);

    // Exchange Station

    public static final BlockRegistryObjectGroup<ExchangeStationBlock, BlockItem, ExchangeStationTile> EXCHANGE_STATION =
            new BlockRegistryObjectGroup<>("exchange_station", ExchangeStationBlock::new, machineItemCreator(), ExchangeStationTile::new).register(BLOCKS, ITEMS, TILES);

    // Pipe
    public static final BlockRegistryObjectGroup<PipeBlock, Item, PipeTile> PIPE =
            new BlockRegistryObjectGroup<>("multi_pipe", PipeBlock::new, null, PipeTile::new).registerWithoutItem(BLOCKS, TILES);

    // Botarium
    public static final BlockRegistryObjectGroup<BotariumMaster, BlockItem, BotariumTile> BOTARIUM =
            new BlockRegistryObjectGroup<>("botarium", BotariumMaster::new, machineItemCreator(), BotariumTile::new).register(BLOCKS, ITEMS, TILES);
    public static final RegistryObject<BotariumTop> BOTARIUM_TOP = BLOCKS.register("botarium_top", BotariumTop::new);
    public static final RegistryObject<TileEntityType<TopEnabledOnlySlave>> BOTARIUM_TOP_TILE = TILES.register("botarium_top", () -> TileEntityType.Builder.of(TopEnabledOnlySlave::new,BOTARIUM_TOP.get())
            .build(null));

    // Arboretum
    public static final BlockRegistryObjectGroup<ArboretumMaster, BlockItem, ArboretumTile> ARBORETUM =
            new BlockRegistryObjectGroup<>("arboretum", ArboretumMaster::new, machineItemCreator(), ArboretumTile::new).register(BLOCKS, ITEMS, TILES);
    public static final RegistryObject<ArboretumTop> ARBORETUM_TOP = BLOCKS.register("arboretum_top", ArboretumTop::new);
    public static final RegistryObject<TileEntityType<TopEnabledOnlySlave>> ARBORETUM_TOP_TILE = TILES.register("arboretum_top", () -> TileEntityType.Builder.of(TopEnabledOnlySlave::new, ARBORETUM_TOP.get())
            .build(null));

    // Ores
    public static final BlockRegistryObjectGroup<Block, BlockItem, TileEntity> ALUMINIUM_ORE =
            new BlockRegistryObjectGroup<>("aluminium_ore", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_ORE)), blockItemCreator(), null).registerWithoutTile(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, TileEntity> COPPER_ORE =
            new BlockRegistryObjectGroup<>("copper_ore", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_ORE)), blockItemCreator(), null).registerWithoutTile(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, TileEntity> LEAD_ORE =
            new BlockRegistryObjectGroup<>("lead_ore", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_ORE)), blockItemCreator(), null).registerWithoutTile(BLOCKS, ITEMS);

    public static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().tab(TECHARIUM));
    }

    public static <B extends MachineBlock> Function<B, BlockItem> machineItemCreator() {
        return block -> new MachineItem(block, new Item.Properties().tab(TECHARIUM));
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILES.register(bus);
    }

}
