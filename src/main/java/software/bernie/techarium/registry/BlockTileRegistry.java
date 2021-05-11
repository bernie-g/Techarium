package software.bernie.techarium.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.BlockRegistryObjectGroup;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.block.botarium.BotariumTop;
import software.bernie.techarium.block.botarium.BotariumMaster;
import software.bernie.techarium.item.MachineItem;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.tile.slaves.TopEnabledOnlySlave;

import java.util.function.Function;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUMS;
@SuppressWarnings("unused")
public class BlockTileRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
            Techarium.ModID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Techarium.ModID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techarium.ModID);

    //Tile
    public static final BlockRegistryObjectGroup<BotariumMaster, BlockItem, BotariumTile> BOTARIUM =
            new BlockRegistryObjectGroup<>("botarium", BotariumMaster::new, machineItemCreator(), BotariumTile::new).register(BLOCKS, ITEMS, TILES);

    public static final RegistryObject<BotariumTop> BOTARIUM_TOP = BLOCKS.register("botarium_top", BotariumTop::new);

    public static final RegistryObject<TileEntityType<TopEnabledOnlySlave>> BOTARIUM_TOP_TILE = TILES.register("botarium_top", () -> TileEntityType.Builder.create(TopEnabledOnlySlave::new,BOTARIUM_TOP.get())
            .build(null));

    public static <B extends MachineBlock> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(TECHARIUMS));
    }

    public static <B extends MachineBlock> Function<B, BlockItem> machineItemCreator() {
        return block -> new MachineItem(block, new Item.Properties().group(TECHARIUMS));
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILES.register(bus);
    }

}
