package software.bernie.techarium.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import software.bernie.techarium.trait.Traits;
import software.bernie.techarium.trait.behaviour.Behaviour;
import software.bernie.techarium.trait.behaviour.IHasBehaviour;
import software.bernie.techarium.trait.block.BlockBehaviour;
import software.bernie.techarium.trait.block.BlockTraits;
import software.bernie.techarium.util.BlockRegion;

import javax.annotation.Nullable;
import java.util.Optional;


public abstract class TechariumBlock<T extends TileEntity> extends RotatableBlock implements IHasBehaviour {

    private final BlockBehaviour behaviour;

    public TechariumBlock(BlockBehaviour behaviour, Properties properties) {
        super(configure(properties, behaviour));
        this.behaviour = behaviour;

        behaviour.tweak(this);
    }

    public static Properties configure(Properties properties, BlockBehaviour behaviour){
        behaviour.tweak(properties);
        return properties;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return behaviour.has(BlockTraits.TileEntityTrait.class);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return behaviour.get(BlockTraits.TileEntityTrait.class).map(BlockTraits.TileEntityTrait::createTileEntity).orElse(null);
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        // god knows why but you have to return true to stop the particles
        return !behaviour.getRequired(BlockTraits.ParticlesTrait.class).isShowBreakParticles();
    }

    @Override
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        // god knows why but you have to return true to stop the particles
        return !behaviour.getRequired(BlockTraits.ParticlesTrait.class).isShowBreakParticles();
    }

    public Optional<Traits.DescriptionTrait> getDescription() {
        return behaviour.get(Traits.DescriptionTrait.class);
    }

    @Override
    public Behaviour getBehaviour() {
        return this.behaviour;
    }

    public boolean canBePlaced(World world, BlockPos pos) {
        BlockRegion region = getBlockSize();
        for (int x = region.xOff; x < region.xSize - region.xOff; x++) {
            for (int y = region.yOff; y < region.ySize - region.yOff; y++) {
                for (int z = region.zOff; z < region.zSize - region.zOff; z++) {
                    if (!world.getBlockState(pos.offset(x, y, z)).getMaterial().isReplaceable()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public BlockRegion getBlockSize() {
        return BlockRegion.FULL_BLOCK;
    }
}
