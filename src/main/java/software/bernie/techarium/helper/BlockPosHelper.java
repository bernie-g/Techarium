package software.bernie.techarium.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class BlockPosHelper {

	public static Vector3d getCenter(BlockPos pos) {
		return new Vector3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
	}
	
	public BlockPos isBlockInside(World world, AxisAlignedBB box, Block block) {
        return isStateInside(world, box, state -> state.getBlock() == block);
	}
	
	public BlockPos isStateInside(World world, AxisAlignedBB box, BlockState state) {
        return isStateInside(world, box, toTest -> toTest == state);
	}
    public BlockPos isStateInside(World world, AxisAlignedBB box, Predicate<BlockState> predicate) {
        int minX = MathHelper.floor(box.minX);
        int maxX = MathHelper.ceil(box.maxX);
        int minY = MathHelper.floor(box.minY);
        int maxY = MathHelper.ceil(box.maxY);
        int minZ = MathHelper.floor(box.minZ);
        int maxZ = MathHelper.ceil(box.maxZ);

        for(int x = minX; x < maxX; ++x) {
            for(int y = minY; y < maxY; ++y) {
                for(int z = minZ; z < maxZ; ++z) {
                    if (predicate.test(world.getBlockState(new BlockPos(x,y,z))))
                        return new BlockPos(x,y,z);
                }
            }
        }
        return null;
    }
	
}	
