package software.bernie.techarium.block.base;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import software.bernie.techarium.data.DataHolder;
import software.bernie.techarium.trait.Traits;
import software.bernie.techarium.trait.behaviour.Behaviour;
import software.bernie.techarium.trait.behaviour.IHasBehaviour;
import software.bernie.techarium.trait.block.BlockBehaviour;
import software.bernie.techarium.trait.block.BlockTraits;

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
        return behaviour.getRequired(BlockTraits.TileEntityTrait.class).createTileEntity();
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
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return super.getCollisionShape(p_220071_1_, p_220071_2_, p_220071_3_, p_220071_4_);
    }

    @Override
    public Behaviour getBehaviour() {
        return this.behaviour;
    }
}
