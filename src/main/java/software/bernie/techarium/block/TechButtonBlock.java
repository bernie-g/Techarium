package software.bernie.techarium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StoneButtonBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import software.bernie.techarium.util.Utils;

public class TechButtonBlock extends StoneButtonBlock {
    protected static final VoxelShape CEILING_AABB_X = Block.box(2.0D, 13.0D, 3.5D, 14.0D, 16.0D, 12.5D);
    protected static final VoxelShape CEILING_AABB_Z = Block.box(3.5D, 13.0D, 2.0D, 12.5D, 16.0D, 14.0D);
    protected static final VoxelShape FLOOR_AABB_X = Block.box(2.0D, 0.0D, 3.5D, 14.0D, 3.0D, 12.5D);
    protected static final VoxelShape FLOOR_AABB_Z = Block.box(3.5D, 0.0D, 2.0D, 12.5D, 3.0D, 14.0D);

    public TechButtonBlock(Properties p_i48315_1_) {
        super(p_i48315_1_);
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
                               ISelectionContext p_220053_4_) {
        Direction direction = p_220053_1_.getValue(FACING);
        switch (p_220053_1_.getValue(FACE)) {
            case FLOOR:
                if (direction.getAxis() == Direction.Axis.X) {
                    return FLOOR_AABB_Z;
                }
                return FLOOR_AABB_X;
            case WALL:
                return Utils.rotateVoxelShape(Utils.rotateVoxelShape(FLOOR_AABB_X, Direction.UP), direction);
            case CEILING:
            default:
                if (direction.getAxis() == Direction.Axis.X) {
                    return CEILING_AABB_X;
                } else {
                    return CEILING_AABB_Z;
                }
        }
    }
}
