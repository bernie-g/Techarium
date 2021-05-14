package software.bernie.techarium.integration.hwyla;

import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.tile.base.MachineSlaveTile;

public class HwylaProvider implements IServerDataProvider<TileEntity> {
    @Override
    public void appendServerData(CompoundNBT data, ServerPlayerEntity player, World world, TileEntity tile) {
        if (tile instanceof MachineSlaveTile) {
            MachineSlaveTile slaveTile = (MachineSlaveTile) tile;
            TileEntity masterTile = world.getTileEntity(slaveTile.getMasterPos());
            if (masterTile instanceof MachineMasterTile) {
                addProbeInfo((MachineMasterTile) masterTile, data);
            }
        } else if (tile instanceof MachineMasterTile) {
            addProbeInfo((MachineMasterTile) tile, data);
        }
    }
    public void addProbeInfo(MachineMasterTile tile, CompoundNBT data) {
        tile.getController().getMultiProgressBar().getProgressBarAddons().forEach(progressBarAddon -> {
            if (progressBarAddon.getName().equals("techarium.gui.mainprogress")) {
                data.putInt("current", progressBarAddon.getProgress());
                data.putInt("max", progressBarAddon.getMaxProgress());
            }
        });
    }
}
