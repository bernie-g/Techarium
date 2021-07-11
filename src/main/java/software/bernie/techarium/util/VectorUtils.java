package software.bernie.techarium.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3i;

public class VectorUtils {
    public static Vector3i rotateVector2D(Vector3i vec, Direction direction) {
        double theta = 0;
        switch (direction) {
            case EAST:
                theta = 90;
                break;
            case WEST:
                theta = 270;
                break;
            case SOUTH:
                theta = 180;
                break;
            case NORTH:
            default:
                theta = 0;
                break;
        }

        theta = Math.toRadians(theta);
        double x = ((double)vec.getX() * Math.cos(theta)) - ((double)vec.getZ() * Math.sin(theta));
        double y = vec.getY();
        double z = ((double)vec.getX() * Math.sin(theta)) + ((double)vec.getZ() * Math.cos(theta));
        return new Vector3i(Math.rint(x), y, Math.rint(z));
    }

    public static Vector3i mul(Vector3i vec, int scale){
        return mul(vec, scale, scale, scale);
    }

    public static Vector3i mul(Vector3i vec, int scaleX, int scaleY, int scaleZ){
        return new Vector3i(vec.getX() * scaleX, vec.getY() * scaleY, vec.getZ() * scaleZ);
    }
}
