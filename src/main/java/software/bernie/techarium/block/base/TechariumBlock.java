package software.bernie.techarium.block.base;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
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

public abstract class TechariumBlock<T extends TileEntity> extends Block implements IHasBehaviour {

    private final BlockBehaviour behaviour;

    public TechariumBlock(BlockBehaviour behaviour, Properties properties) {
        super(configure(properties, behaviour));
        this.behaviour = behaviour;

        //Copied from the Block constructor. This is hacky but I can't figure out a better way to do this as createBlockStateDefinition() needs this block's behaviour, which hasn't been set yet.
        StateContainer.Builder<Block, BlockState> builder = new StateContainer.Builder<>(this);
        this.createBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any());


        behaviour.tweak(this);
    }

    public static Properties configure(Properties properties, BlockBehaviour behaviour) {
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
        return behaviour.get(BlockTraits.TileEntityTrait.class).map(BlockTraits.TileEntityTrait::createTileEntity)
                .orElse(null);
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

    public DirectionProperty getDirectionProperty() {
        return getBehaviour().getRequired(BlockTraits.BlockRotationTrait.class).getDirectionProperty();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        return getBehaviour().getRequired(BlockTraits.BlockRotationTrait.class).getStateForPlacement(this, useContext);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        if (this.behaviour == null) {
            return;
        }

        getBehaviour().get(BlockTraits.PropertyTrait.class).ifPresent(trait -> trait.createBlockStateDefinition(builder));
        getBehaviour().get(BlockTraits.BlockRotationTrait.class).ifPresent(trait -> trait.createBlockStateDefinition(builder));
    }

    @Override
    public Behaviour getBehaviour() {
        return this.behaviour;
    }
}
