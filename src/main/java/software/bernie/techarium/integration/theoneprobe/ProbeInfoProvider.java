package software.bernie.techarium.integration.theoneprobe;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.tile.base.MachineSlaveTile;
import software.bernie.techarium.tile.base.TechariumTileBase;
import software.bernie.techarium.trait.tile.TileTraits;

public class ProbeInfoProvider implements IProbeInfoProvider {
    @Override
    public String getID() {
        return Techarium.ModID + ":machineBlock";
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData iProbeHitData) {
        TileEntity tile = world.getBlockEntity(iProbeHitData.getPos());
        if (tile instanceof MachineSlaveTile) {
            MachineSlaveTile slaveTile = (MachineSlaveTile) tile;
            TileEntity masterTile = world.getBlockEntity(slaveTile.getMasterPos());
            if (masterTile instanceof MachineMasterTile) {
                addProbeInfo((MachineMasterTile) masterTile, iProbeInfo);
            }
        } else if (tile instanceof MachineMasterTile) {
            addProbeInfo((MachineMasterTile) tile, iProbeInfo);
        } else if (tile instanceof TechariumTileBase) {
            ((TechariumTileBase) tile).getBehaviour().get(TileTraits.PowerTrait.class).ifPresent(trait -> {
                iProbeInfo.progress(trait.getEnergyStorage().getEnergyStored(), trait.getEnergyStorage().getMaxEnergyStored(), iProbeInfo.defaultProgressStyle().suffix("RF").filledColor(Config.rfbarFilledColor).alternateFilledColor(Config.rfbarAlternateFilledColor).borderColor(Config.rfbarBorderColor).numberFormat((NumberFormat)Config.rfFormat.get()));
            });
        }
    }

    public void addProbeInfo(MachineMasterTile tile, IProbeInfo info) {
        tile.getController().getMultiProgressBar().getProgressBarAddons().forEach(progressBarAddon -> {
            if (progressBarAddon.getName().equals("techarium.gui.mainprogress")) {
                info.progress(progressBarAddon.getProgress() / 20, progressBarAddon.getMaxProgress() / 20, info.defaultProgressStyle().filledColor(-39424).alternateFilledColor(-39424).suffix("s/" + progressBarAddon.getMaxProgress() / 20 + "s").prefix(LangRegistry.topProgressETA.get().getString()));
            }
        });
    }

}
