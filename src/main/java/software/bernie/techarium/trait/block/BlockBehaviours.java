package software.bernie.techarium.trait.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShapes;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
import software.bernie.techarium.tile.slaves.SlaveTile;

public class BlockBehaviours {
    public static BlockBehaviour botarium = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .tileEntity(BotariumTile.class)
            .description(LangRegistry.botariumDescription)
            .shape(VoxelShapes.or(
                    Block.box(0.0D, 0.0D, 0.0D, 32.0D, 32.0D, 32.0D),
                    Block.box(30.0D, 40.0D, 17.0D, 40.0D, 60.0D, 30.0D)
            ))
            .with()
            .build();

    public static BlockBehaviour arboretum = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .tileEntity(ArboretumTile.class)
            .description(LangRegistry.arboretumDescription)
            .shape(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D))
            .build();

    public static BlockBehaviour exchangeStation = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .animatedModel()
            .tileEntity(ExchangeStationTile.class)
            .description(LangRegistry.exchangeStationDescription)
            .shape(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D))
            .build();

    public static BlockBehaviour createSlave(BlockBehaviour masterBehaviour) {
        // copy the master block's material trait to the slave
        return masterBehaviour.getBaseTrait(BlockTraits.MaterialTrait.class).map(trait -> new BlockBehaviour.Builder()
                .composeFrom(BlockPartialBehaviours.partialSlaveBlock)
                .with(trait)
                .with(new SlaveBlockTrait(masterBehaviour))
                .tileEntity(SlaveTile.class)
                .build()).orElseThrow(IllegalStateException::new);
    }
}
