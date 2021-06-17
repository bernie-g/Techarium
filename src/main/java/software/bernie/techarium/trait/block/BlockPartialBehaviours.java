package software.bernie.techarium.trait.block;

import software.bernie.techarium.tile.slaves.TopEnabledOnlySlave;
import software.bernie.techarium.trait.behaviour.PartialBehaviour;

public class BlockPartialBehaviours {
    public static PartialBehaviour partialMaterial = new BlockBehaviour.Builder()
            .partial();

    public static PartialBehaviour partialBaseBlock = new BlockBehaviour.Builder().composeFrom(partialMaterial)
            .requiredTraits(BlockTraits.ParticlesTrait.class)
            .requiredTraits(BlockTraits.MaterialTrait.class)
            .requiredTraits(BlockTraits.BlockRenderTypeTrait.class)
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

    public static PartialBehaviour partialSlaveBlock = new BlockBehaviour.Builder().composeFrom(partialBaseBlock)
            .showBreakParticles(false)
            .tileEntity(TopEnabledOnlySlave.class)
            .animatedModel()
            .partial();
}
