package software.bernie.techarium.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import software.bernie.techarium.tile.base.MachineTileBase;
import software.bernie.techarium.trait.block.BlockBehaviour;

public class MachineBlockRotationXYZ<T extends MachineTileBase> extends MachineBlock<T> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public MachineBlockRotationXYZ(BlockBehaviour behaviour, Properties properties) {
        super(behaviour, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
