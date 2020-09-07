package software.bernie.techariumbotanica.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.api.BlockRegistryObjectGroup;
import software.bernie.techariumbotanica.block.BotariumTop;
import software.bernie.techariumbotanica.block.BotaniumMaster;
import software.bernie.techariumbotanica.tile.BotaniumTile;

import java.util.function.Function;

import static software.bernie.techariumbotanica.registry.ItemGroupRegistry.TECHARIUMS;
@SuppressWarnings("unused")
public class BlockTileRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
            TechariumBotanica.ModID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            TechariumBotanica.ModID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TechariumBotanica.ModID);

    //Tile
    public static final BlockRegistryObjectGroup<BotaniumMaster, BlockItem, BotaniumTile> BOTANIUM =
            new BlockRegistryObjectGroup<>("botanium", BotaniumMaster::new, blockItemCreator(), BotaniumTile::new).register(BLOCKS, ITEMS, TILES);


    //Blocks
    public static final RegistryObject<Block> BOTARIUM_TOP = BLOCKS.register("botarium_top", BotariumTop::new);

    public static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(TECHARIUMS));
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILES.register(bus);

    }

}
