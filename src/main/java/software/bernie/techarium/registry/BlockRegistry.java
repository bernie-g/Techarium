package software.bernie.techarium.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.BlockRegistryObjectGroup;
import software.bernie.techarium.block.arboretum.ArboretumMaster;
import software.bernie.techarium.block.arboretum.ArboretumTop;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.block.botarium.BotariumTop;
import software.bernie.techarium.block.coils.MagneticCoilBlock;
import software.bernie.techarium.block.botarium.BotariumMaster;
import software.bernie.techarium.block.depot.DepotBlock;
import software.bernie.techarium.block.exchangestation.ExchangeStationBlock;
import software.bernie.techarium.block.gravmagnet.GravMagnetBlock;
import software.bernie.techarium.block.pipe.PipeBlock;
import software.bernie.techarium.block.voltaicpile.VoltaicPileBlock;
import software.bernie.techarium.item.TechariumBlockItem;
import software.bernie.techarium.item.MachineItem;
import software.bernie.techarium.item.FancyItem;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.tile.depot.DepotTileEntity;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
import software.bernie.techarium.tile.gravmagnet.GravMagnetTile;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;
import software.bernie.techarium.tile.pipe.PipeTile;
import software.bernie.techarium.tile.slaves.TopEnabledOnlySlave;
import software.bernie.techarium.util.Utils;
import software.bernie.techarium.tile.voltaicpile.VoltaicPileTile;

import java.util.function.Function;
import java.util.function.Supplier;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUM;
@SuppressWarnings("unused")
public class BlockRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
            Techarium.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Techarium.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techarium.MOD_ID);

    // Exchange Station

    public static final BlockRegistryObjectGroup<ExchangeStationBlock, BlockItem, ExchangeStationTile> EXCHANGE_STATION =
            new BlockRegistryObjectGroup<>("exchange_station", ExchangeStationBlock::new, machineItemCreator(), ExchangeStationTile::new).register(BLOCKS, ITEMS, TILES);

    
    //MAGNETIC STUFF
    public static final BlockRegistryObjectGroup<GravMagnetBlock, BlockItem, GravMagnetTile> GRAVMAGNET =
            new BlockRegistryObjectGroup<>("gravmagnet", GravMagnetBlock::new, fancyItemCreator(event -> {
                        // event.getController().setAnimation(new AnimationBuilder().addAnimation("pushing", true));
                        return PlayState.CONTINUE;
                    },
                    Utils.rl("geo/gravmagnet/gravmagnet.geo.json"),
                    Utils.rl("textures/block/animated/gravmagnet_push.png"),
                    Utils.rl("animations/gravmagnet.animation.json")), GravMagnetTile::new)
                        .register(BLOCKS, ITEMS, TILES);
    
    public static final BlockRegistryObjectGroup<MagneticCoilBlock, BlockItem, MagneticCoilTile> MAGNETIC_COIL =
            new BlockRegistryObjectGroup<>("magneticcoil", MagneticCoilBlock::new, fancyItemCreator(event -> {
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
                        return PlayState.CONTINUE;
                    },
                    Utils.rl("geo/magneticcoil/magneticcoil.geo.json"),
                    Utils.rl("textures/block/animated/magneticcoil/magneticcoil_support.png"),
                    Utils.rl("animations/magneticcoil.animation.json")), MagneticCoilTile::new)
                        .register(BLOCKS, ITEMS, TILES);
    

    // Depot
    public static final BlockRegistryObjectGroup<DepotBlock, BlockItem, DepotTileEntity> DEPOT =
            new BlockRegistryObjectGroup<>("depot", DepotBlock::new, machineItemCreator(), DepotTileEntity::new)
                        .register(BLOCKS, ITEMS, TILES);

    // Pipe
    public static final BlockRegistryObjectGroup<PipeBlock, Item, PipeTile> PIPE =
            new BlockRegistryObjectGroup<>("multi_pipe", PipeBlock::new, null, PipeTile::new).registerWithoutItem(BLOCKS, TILES);

    // Botarium
    public static final BlockRegistryObjectGroup<BotariumMaster, BlockItem, BotariumTile> BOTARIUM =
            new BlockRegistryObjectGroup<>("botarium", BotariumMaster::new, fancyItemCreator(event -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("Botarium.anim.idle", true));
                return PlayState.CONTINUE;
            },
                Utils.rl("geo/botarium/botarium.geo.json"),
                Utils.rl("textures/block/animated/botarium.png"),
                Utils.rl("animations/botarium.animation.json")), BotariumTile::new)
                    .register(BLOCKS, ITEMS, TILES);

    public static final RegistryObject<BotariumTop> BOTARIUM_TOP = BLOCKS.register("botarium_top", BotariumTop::new);
    public static final RegistryObject<TileEntityType<TopEnabledOnlySlave>> BOTARIUM_TOP_TILE = TILES.register("botarium_top", () -> TileEntityType.Builder.of(TopEnabledOnlySlave::new,BOTARIUM_TOP.get())
            .build(null));

    // Arboretum
    public static final BlockRegistryObjectGroup<ArboretumMaster, BlockItem, ArboretumTile> ARBORETUM =
            new BlockRegistryObjectGroup<>("arboretum", ArboretumMaster::new, fancyItemCreator(event -> {
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("Arboretum.anim.idle", true));
                        return PlayState.CONTINUE;
                    },
                    Utils.rl("geo/arboretum/arboretum.geo.json"),
                    Utils.rl("textures/block/animated/arboretum.png"),
                    Utils.rl("animations/arboretum.animation.json")), ArboretumTile::new)
                        .register(BLOCKS, ITEMS, TILES);
    public static final RegistryObject<ArboretumTop> ARBORETUM_TOP = BLOCKS.register("arboretum_top", ArboretumTop::new);
    public static final RegistryObject<TileEntityType<TopEnabledOnlySlave>> ARBORETUM_TOP_TILE = TILES.register("arboretum_top", () -> TileEntityType.Builder.of(TopEnabledOnlySlave::new, ARBORETUM_TOP.get())
            .build(null));

    // Voltaic Pile
    public static final BlockRegistryObjectGroup<VoltaicPileBlock, BlockItem, VoltaicPileTile> VOLTAIC_PILE =
            new BlockRegistryObjectGroup<>("voltaic_pile", VoltaicPileBlock::new, techariumBlockItemCreator(), VoltaicPileTile::new).register(BLOCKS, ITEMS, TILES);

    // Ores + Blocks
    public static final RegistryObject<Block> ALUMINIUM_ORE = registerBlock("aluminium_ore", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> COPPER_ORE = registerBlock("copper_ore", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> LEAD_ORE = registerBlock("lead_ore", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> NICKEL_ORE = registerBlock("nickel_ore", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> ZINC_ORE = registerBlock("zinc_ore", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> ALUMINIUM_BLOCK = registerBlock("aluminium_block", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> COPPER_BLOCK = registerBlock("copper_block", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> LEAD_BLOCK = registerBlock("lead_block", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> NICKEL_BLOCK = registerBlock("nickel_block", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> ZINC_BLOCK = registerBlock("zinc_block", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> reg = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(reg.get(), new Item.Properties().tab(TECHARIUM)));
        return reg;
    }

    public static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().tab(TECHARIUM));
    }

    public static <B extends TechariumBlock> Function<B, BlockItem> techariumBlockItemCreator() {
        return block -> new TechariumBlockItem(block, new Item.Properties().tab(TECHARIUM));
    }

    public static <B extends MachineBlock> Function<B, BlockItem> machineItemCreator() {
        return block -> new MachineItem(block, new Item.Properties().tab(TECHARIUM));
    }

    // INTENDED FOR MULTI-BLOCK MACHINES!
    public static <B extends MachineBlock> Function<B, BlockItem> fancyItemCreator(Function<AnimationEvent<? extends Item>, PlayState> animationPredicate, ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        return block -> new FancyItem(block, animationPredicate, model, texture, animation);
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILES.register(bus);
    }

}
