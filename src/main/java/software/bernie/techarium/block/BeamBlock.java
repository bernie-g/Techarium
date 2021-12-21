package software.bernie.techarium.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.registry.BlockRegistry;

import java.util.HashMap;
import java.util.Map;

public class BeamBlock extends Block {
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("up");
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("down");

    private static final Map<BlockState, VoxelShape> shapes = new HashMap<>();

    private static VoxelShape beam = box(5,0,5,11,16,11);
    private static VoxelShape foot = VoxelShapes.or(
            box(2,0,2,14,1,14),
            box(4,1,4,12,9,12));
    private static VoxelShape head = VoxelShapes.or(
            box(2,15,2,14,16,14),
            box(4,7,4,12,15,12));

    public BeamBlock() {
        super(AbstractBlock.Properties.of(Material.METAL).noOcclusion());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getState(context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction direction, BlockState directionState, IWorld world, BlockPos currentPos, BlockPos directionPos) {
        return getState(world, currentPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return shapes.computeIfAbsent(state,unused -> createShape(state));
    }

    VoxelShape createShape(BlockState state) {
        VoxelShape shape = beam;
        if (!state.getValue(CONNECTED_UP))
            shape = VoxelShapes.or(shape, head);
        if (!state.getValue(CONNECTED_DOWN))
            shape = VoxelShapes.or(shape, foot);
        return shape.optimize();
    }

    private BlockState getState(IWorld world, BlockPos pos) {
        return defaultBlockState()
                .setValue(CONNECTED_UP, hasBeamInDirection(world, pos, Direction.UP))
                .setValue(CONNECTED_DOWN, hasBeamInDirection(world, pos, Direction.DOWN));
    }

    private static boolean hasBeamInDirection(IWorld world, BlockPos pos, Direction direction) {
        return world.getBlockState(pos.relative(direction)).getBlock() == BlockRegistry.BEAM.get();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(CONNECTED_UP);
        stateBuilder.add(CONNECTED_DOWN);
    }
}
