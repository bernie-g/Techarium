package software.bernie.techarium.tile.base;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import software.bernie.techarium.block.base.MachineBlockRotation;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;

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
        BlockState state = this.level.getBlockState(this.worldPosition);
        if (state.getBlock() instanceof MachineBlockRotation) {
        	DirectionProperty direction = ((MachineBlockRotation) state.getBlock()).getDirectionProperty();
        	return state.getValue(direction);
        }
        return Direction.NORTH;
    }

    public Map<Side, FaceConfig> getFaceConfigs() {
        return sideFaceConfigs;
    }

    public abstract ActionResultType onTileActivated(PlayerEntity player);
}
