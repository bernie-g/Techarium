package software.bernie.techarium.trait.block;

import net.minecraft.block.BlockRenderType;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
import software.bernie.techarium.tile.gravmagnet.GravMagnetTile;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;
import software.bernie.techarium.tile.voltaicpile.VoltaicPileTile;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.trait.Traits;

public class BlockBehaviours {
    public static BlockBehaviour botarium = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .tileEntity(BotariumTile.class)
            .description(LangRegistry.botariumDescription)
            .rotation(BlockTraits.RotationType.XZ)
            .build();

    public static BlockBehaviour arboretum = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .tileEntity(ArboretumTile.class)
            .description(LangRegistry.arboretumDescription)
            .rotation(BlockTraits.RotationType.XZ)
            .build();

    public static BlockBehaviour exchangeStation = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .animatedModel()
            .tileEntity(ExchangeStationTile.class)
            .rotation(BlockTraits.RotationType.XZ)
            .description(LangRegistry.exchangeDescription)
            .build();
    
    public static BlockBehaviour gravMagnet = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .animatedModel()
            .tileEntity(GravMagnetTile.class)
            .rotation(BlockTraits.RotationType.XYZ)
            .description(LangRegistry.gravMagnetDescription)
            .build();
    
    public static BlockBehaviour magenticCoil = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialMachineBlock)
            .animatedModel()
            .tileEntity(MagneticCoilTile.class)
            .rotation(BlockTraits.RotationType.XYZ)
            .description(LangRegistry.magneticCoilDescription)
            .build();

    public static BlockBehaviour voltaicPile = new BlockBehaviour.Builder()
            .composeFrom(BlockPartialBehaviours.partialTileBlock)
            .tileEntity(VoltaicPileTile.class)
            .with(new BlockTraits.MachineMaterialTrait())
            .description(LangRegistry.voltaicPileDescription)
            .build();

    public static BlockBehaviour createSlave(BlockBehaviour masterBehaviour) {
        // copy the master block's material trait to the slave
        return masterBehaviour.getBaseTrait(BlockTraits.MaterialTrait.class).map(trait -> new BlockBehaviour.Builder()
                .composeFrom(BlockPartialBehaviours.partialSlaveBlock)
                .with(trait)
                .with(masterBehaviour.getRequired(BlockTraits.BlockRotationTrait.class))
                .build()).orElseThrow(IllegalStateException::new);
    }
}
