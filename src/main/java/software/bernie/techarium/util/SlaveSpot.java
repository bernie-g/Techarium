package software.bernie.techarium.util;

import lombok.Data;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3i;

@Data
public class SlaveSpot {
    private final Vector3i offset;
    private final VoxelShape voxelShape;
}
