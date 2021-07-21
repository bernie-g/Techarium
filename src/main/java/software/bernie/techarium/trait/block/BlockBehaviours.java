package software.bernie.techarium.trait.block;

import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
import software.bernie.techarium.tile.gravmagnet.GravMagnetTile;

public class BlockBehaviours {
    public static BlockBehaviour botarium = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .tileEntity(BotariumTile.class)
            .description(LangRegistry.botariumDescription)
            .rotation(BlockRotationTrait.RotationType.XZ)
            .build();

    public static BlockBehaviour arboretum = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .tileEntity(ArboretumTile.class)
            .description(LangRegistry.arboretumDescription)
            .rotation(BlockRotationTrait.RotationType.XZ)
            .build();

    public static BlockBehaviour exchangeStation = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .animatedModel()
            .tileEntity(ExchangeStationTile.class)
            .rotation(BlockRotationTrait.RotationType.XZ)
            .description(LangRegistry.exchangeDescription)
            .build();
    
    public static BlockBehaviour gravMagnet = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .animatedModel()
            .tileEntity(GravMagnetTile.class)
            .rotation(BlockRotationTrait.RotationType.XYZ)
            .description(LangRegistry.gravMagnetDescription)
            .build();

    public static BlockBehaviour createSlave(BlockBehaviour masterBehaviour) {
        // copy the master block's material trait to the slave
        return masterBehaviour.getBaseTrait(BlockTraits.MaterialTrait.class).map(trait -> new BlockBehaviour.Builder()
                .composeFrom(BlockPartialBehaviours.partialSlaveBlock)
                .with(trait)
                .with(masterBehaviour.getRequired(BlockRotationTrait.class))
                .build()).orElseThrow(IllegalStateException::new);
    }
}
