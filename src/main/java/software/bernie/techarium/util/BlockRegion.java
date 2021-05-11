package software.bernie.techarium.util;

public class BlockRegion {
    public static BlockRegion FULL_BLOCK = new BlockRegion(1,1,1);

    public final int xOff;
    public final int yOff;
    public final int zOff;
    public final int xSize;
    public final int ySize;
    public final int zSize;

    public BlockRegion(int xSize, int ySize, int zSize) {
        this(-xSize/2,0,-zSize/2, xSize, ySize, zSize);
    }

    public BlockRegion(int xOff, int yOff, int zOff, int xSize, int ySize, int zSize) {
        this.xOff = xOff;
        this.yOff = yOff;
        this.zOff = zOff;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }
}
