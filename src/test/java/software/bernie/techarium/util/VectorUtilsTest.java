package software.bernie.techarium.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorUtilsTest {

    @Test
    public void testRotateVec(){
        Vector3i vector3i = VectorUtils.rotateVector2D(new Vector3i(-2, 0, 1), Direction.SOUTH);
        assertEquals(vector3i, new Vector3i(2, 0, -1));
        int x = 5;
    }
}