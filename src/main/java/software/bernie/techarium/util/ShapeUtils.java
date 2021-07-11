package software.bernie.techarium.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class ShapeUtils {

    private static final VoxelShape BLOCK = VoxelShapes.box(0, 0, 0, 1, 1, 1);

    public static List<SlaveSpot> getSlaveOffsets(VoxelShape shape) {
        AxisAlignedBB aabb = shape.bounds();

        double iMin = aabb.minX;
        double jMin = aabb.minY;
        double kMin = aabb.minZ;


        double iMax = aabb.maxX;
        double jMax = aabb.maxY;
        double kMax = aabb.maxZ;

        List<SlaveSpot> offsets = new ArrayList<>();

        //loop through each possible block in the voxelshape and add a slave there
        for (double i = iMin; i < iMax; i += 1) {
            for (double j = jMin; j < jMax; j += 1) {
                for (double k = kMin; k < kMax; k += 1) {
                    if(i == 0 && j == 0 && k == 0){
                        // skip the master block
                        continue;
                    }
                    VoxelShape movedBlock = BLOCK.move(i, j, k);
                    VoxelShape changed = VoxelShapes
                            .join(movedBlock, shape, IBooleanFunction.ONLY_FIRST);
                    if (!areVoxelShapesEqual(changed, movedBlock)) {
                        Vector3i offset = new Vector3i(i, j, k);
                        offsets.add(new SlaveSpot(offset, shape.move(-i, -j, -k)));
                    }
                }
            }
        }
        return offsets;
    }

    public static boolean areVoxelShapesEqual(VoxelShape shape1, VoxelShape shape2){
        List<AxisAlignedBB> aabbs1 = shape1.toAabbs();
        List<AxisAlignedBB> aabbs2 = shape2.toAabbs();

        return aabbs1.containsAll(aabbs2) && aabbs2.containsAll(aabbs1);
    }

}
