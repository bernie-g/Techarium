package software.bernie.techarium.util;

import net.minecraft.util.Direction;
import software.bernie.techarium.machine.sideness.Side;

import javax.annotation.Nullable;

public class StaticHandler {

    public static Side getSideFromDirection(@Nullable Direction face,Direction rotated){
        if (face == null) {
            return null;
        } else if (face == Direction.UP) {
            return Side.UP;
        } else if (face == Direction.DOWN) {
            return Side.DOWN;
        } else if (rotated == face) {
            return Side.FRONT;
        } else if (rotated == face.getOpposite()) {
            return Side.BACK;
        } else if (rotated == face.rotateYCCW()) {
            return Side.LEFT;
        } else {
            return rotated == face.rotateY() ? Side.RIGHT : Side.DOWN;
        }
    }

}
