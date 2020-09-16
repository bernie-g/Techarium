package software.bernie.techarium.machine.addon.inventory;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.widget.Widget;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.machine.screen.widget.DrawableWidget;
import software.bernie.techarium.machine.screen.widget.EnergyAutoWidget;
import software.bernie.techarium.tile.base.MachineMasterTile;

import java.util.ArrayList;
import java.util.List;

public class DrawableInventoryAddon extends InventoryAddon {

    private final IDrawable background;
    private final int backgroundXPos;
    private final int backgroundYPos;
    private final int backgroundXSize;
    private final int backgroundYSize;
    public int xOffset = 0;
    public int yOffset = 0;
    public int xSizeOffset = 0;
    public int ySizeOffset = 0;

    public DrawableInventoryAddon(MachineMasterTile tile, String name, int xPos, int yPos, IDrawable bg,int bgX,int bgY, int bgSX, int bgSY, int slots) {
        super(tile, name, xPos, yPos, slots);
        this.background = bg;
        this.backgroundXPos = bgX;
        this.backgroundYPos = bgY;
        this.backgroundXSize = bgSX;
        this.backgroundYSize = bgSY;
    }

    public IDrawable getBackground() {
        return background;
    }

    public int getBackgroundXPos() {
        return backgroundXPos;
    }

    public int getBackgroundXSize() {
        return backgroundXSize;
    }

    public int getBackgroundYPos() {
        return backgroundYPos;
    }

    public int getBackgroundYSize() {
        return backgroundYSize;
    }

    @Override
    public List<IFactory<? extends Widget>> getGuiWidgets() {
        return Lists.newArrayList(() -> {
            DrawableWidget widget = new DrawableWidget(getMachineTile().getActiveController(), getBackground(), getBackgroundXPos(), getBackgroundYPos(), getBackgroundXSize(), getBackgroundYSize(), "inventory");
            widget.xOffset = xOffset;
            widget.yOffset = yOffset;
            widget.xSizeOffset = xSizeOffset;
            widget.ySizeOffset = ySizeOffset;
            return widget;
        });
    }
}
