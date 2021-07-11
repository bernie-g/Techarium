package software.bernie.techarium.trait.block;

import software.bernie.techarium.tile.slaves.SlaveTile;
import software.bernie.techarium.trait.behaviour.PartialBehaviour;

public class BlockPartialBehaviours {
    public static PartialBehaviour partialBaseBlock = new BlockBehaviour.Builder()
            .requiredTraits(BlockTraits.ParticlesTrait.class)
            .requiredTraits(BlockTraits.MaterialTrait.class)
            .requiredTraits(BlockTraits.BlockRenderTypeTrait.class)
            .requiredTraits(BlockTraits.VoxelShapeTrait.class)
            .requiredTraits(MasterBlockTrait.class)
            .with(new MasterBlockTrait())
            .staticModel()
            .showBreakParticles(true)
            .partial();

    public static PartialBehaviour partialTileBlock = new BlockBehaviour.Builder().composeFrom(partialBaseBlock)
            .requiredTraits(BlockTraits.TileEntityTrait.class)
            .partial();

    public static PartialBehaviour partialMachineBlock = new BlockBehaviour.Builder().composeFrom(partialTileBlock)
            .with(new BlockTraits.MachineMaterialTrait())
            .animatedModel()
            .partial();

    public static PartialBehaviour partialSlaveBlock = new BlockBehaviour.Builder()
            .requiredTraits(SlaveBlockTrait.class)
            .showBreakParticles(false)
            .tileEntity(SlaveTile.class)
            .animatedModel()
            .partial();
}
