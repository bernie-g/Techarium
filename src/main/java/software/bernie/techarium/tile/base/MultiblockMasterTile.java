package software.bernie.techarium.tile.base;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.machine.interfaces.recipe.IMachineRecipe;

import java.util.HashMap;
import java.util.Map;


public abstract class MultiblockMasterTile<T extends IMachineRecipe> extends MachineMasterTile<T> {

    private Map<BlockPos, MachineBlock<?>> machineSlaveLocations;

    public MultiblockMasterTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        machineSlaveLocations = getMachineSlaveLocations();
    }

    public Map<BlockPos, MachineBlock<?>> getMachineSlaveLocations() {
        return new HashMap<>();
    }

    public void placeSlaves() {
        if (level != null) {
            for (BlockPos pos : machineSlaveLocations.keySet()) {
                level.setBlockAndUpdate(this.worldPosition.offset(pos.getX(), pos.getY(), pos.getZ()), machineSlaveLocations.get(pos).defaultBlockState());
                TileEntity tile = level.getBlockEntity(this.worldPosition.offset(pos.getX(), pos.getY(), pos.getZ()));
                if (tile instanceof MachineSlaveTile) {
                    MachineSlaveTile slave = (MachineSlaveTile) tile;
                    slave.setMasterPos(this.worldPosition);
                }
            }
        }
    }

    public void destroySlaves() {
        if (level != null) {
            for (BlockPos pos : machineSlaveLocations.keySet()) {
                if (level.getBlockEntity(this.worldPosition.offset(pos.getX(), pos.getY(), pos.getZ())) instanceof MachineSlaveTile) {
                    level.destroyBlock(this.worldPosition.offset(pos.getX(), pos.getY(), pos.getZ()), false);
                }
            }
        }
    }

    @Override
    public void masterHandleDestruction() {
        super.masterHandleDestruction();
        destroySlaves();
        if(level != null)
        level.destroyBlock(worldPosition,true);
    }
}
