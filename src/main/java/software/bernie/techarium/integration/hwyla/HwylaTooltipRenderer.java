package software.bernie.techarium.integration.hwyla;

import mcp.mobius.waila.api.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.tile.base.MachineSlaveTile;
import software.bernie.techarium.tile.base.MachineTileBase;

import java.util.List;

public class HwylaTooltipRenderer implements IComponentProvider {

    public static final HwylaTooltipRenderer INSTANCE = new HwylaTooltipRenderer();

    private HwylaTooltipRenderer() {

    }

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {

        if (accessor.getTileEntity() instanceof MachineMasterTile) {
            appendToolTip(tooltip, (MachineMasterTile) accessor.getTileEntity());
        } else if (accessor.getTileEntity() instanceof MachineSlaveTile) {
            TileEntity masterTile = accessor.getWorld().getBlockEntity(((MachineSlaveTile)accessor.getTileEntity()).getMasterPos());
            if (masterTile instanceof MachineMasterTile) {
                appendToolTip(tooltip, (MachineMasterTile) masterTile);
            }
        }
    }

    private void appendToolTip(List<ITextComponent> tooltip, MachineMasterTile tile) {
        if (tile.getController().getCurrentRecipe() == null) {
            tooltip.add(new TranslationTextComponent("hwyla.techarium.progress.no_recipe"));
        } else {
            tile.getController().getMultiProgressBar().getProgressBarAddons().forEach(progressBarAddon -> {
                if (progressBarAddon.getName().equals("techarium.gui.mainprogress")) {
                    tooltip.add(new TranslationTextComponent("hwyla.techarium.progress.eta", (progressBarAddon.getMaxProgress() - progressBarAddon.getProgress()) / 20));
                }
            });
        }
    }
}
