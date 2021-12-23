package software.bernie.techarium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import software.bernie.techarium.util.Utils;

public class TechLeverBlock extends LeverBlock {
    protected static final VoxelShape UP_AABB_Z = Block.box(3.0D, 0.0D, 2.0D, 13D, 3.0D, 14.0D);
    protected static final VoxelShape UP_AABB_X = Block.box(2.0D, 0.0D, 3.0D, 14.0D, 3.0D, 13.0D);
    protected static final VoxelShape DOWN_AABB_Z = Block.box(3.0D, 13.0D, 2.0D, 13D, 16.0D, 14.0D);
    protected static final VoxelShape DOWN_AABB_X = Block.box(2.0D, 13.0D, 3.0D, 14.0D, 16.0D, 13.0D);

    public TechLeverBlock(Properties p_i48369_1_) {
        super(p_i48369_1_);
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
                               ISelectionContext p_220053_4_) {
        Direction direction = p_220053_1_.getValue(FACING);

        switch (p_220053_1_.getValue(FACE)) {
            case FLOOR:
                switch (p_220053_1_.getValue(FACING).getAxis()) {
                    case X:
                        return UP_AABB_X;
                    case Z:
                    default:
                        return UP_AABB_Z;
                }
            case WALL:
                return Utils
                        .rotateVoxelShape(Utils.rotateVoxelShape(UP_AABB_Z, Direction.UP), direction);
            case CEILING:
            default:
                switch (p_220053_1_.getValue(FACING).getAxis()) {
                    case X:
                        return DOWN_AABB_X;
                    case Z:
                    default:
                        return DOWN_AABB_Z;
                }
        }
    }
}
