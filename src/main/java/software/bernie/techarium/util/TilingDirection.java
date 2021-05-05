package software.bernie.techarium.util;

public enum TilingDirection {
    DOWN_RIGHT(true, true),
    DOWN_LEFT(true, false),
    UP_RIGHT(false, true),
    UP_LEFT(false, false);

    public final boolean down;
    public final boolean right;

    TilingDirection(boolean down, boolean right) {
        this.down = down;
        this.right = right;
    }
}