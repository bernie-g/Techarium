package software.bernie.techarium.tile.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;

import java.util.HashMap;
import java.util.Map;

import static software.bernie.techarium.block.base.RotatableBlock.FACING;

public abstract class MachineTileBase extends TileEntity {

    private final Map<Side, FaceConfig> sideFaceConfigs = setFaceControl();

    public MachineTileBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    protected Map<Side, FaceConfig> setFaceControl(){
        Map<Side, FaceConfig> defaultSetUp = new HashMap<>();
        for (Side side: Side.values()){
            defaultSetUp.put(side,FaceConfig.NONE);
        }
        return defaultSetUp;
    }

    public Direction getFacingDirection() {
        assert this.level != null;
        return this.level.getBlockState(this.worldPosition).hasProperty(FACING) ? this.level.getBlockState(this.worldPosition).getValue(FACING) : Direction.NORTH;
    }

    public Map<Side, FaceConfig> getFaceConfigs() {
        return sideFaceConfigs;
    }

    public abstract ActionResultType onTileActivated(PlayerEntity player);
}
