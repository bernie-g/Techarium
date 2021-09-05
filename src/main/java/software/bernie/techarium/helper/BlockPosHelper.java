package software.bernie.techarium.helper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class BlockPosHelper {

	public static Vector3d getCenter(BlockPos pos) {
		return new Vector3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
	}
	
	public BlockPos isBlockInside(World world, AxisAlignedBB box, Block block) {
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.minY);
        int l = MathHelper.ceil(box.maxY);
        int i1 = MathHelper.floor(box.minZ);
        int j1 = MathHelper.ceil(box.maxZ);

        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                   if (world.getBlockState(mutable.set(k1, l1, i2)).getBlock() == block)
                	   return mutable.set(k1, l1, i2);
                }
            }
        }

        return null;
	}
	
	public BlockPos isStateInside(World world, AxisAlignedBB box, BlockState state) {
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.minY);
        int l = MathHelper.ceil(box.maxY);
        int i1 = MathHelper.floor(box.minZ);
        int j1 = MathHelper.ceil(box.maxZ);

        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                   if (world.getBlockState(mutable.set(k1, l1, i2)) == state)
                	   return mutable.set(k1, l1, i2);
                }
            }
        }
        return null;
	}
	
}	
