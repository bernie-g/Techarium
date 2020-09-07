package software.bernie.techariumbotanica.machine.screen.widget;

import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import software.bernie.techariumbotanica.machine.addon.energy.EnergyStorageAddon;

public class EnergyAutoWidget extends Widget {

    private final EnergyStorageAddon addon;

    public EnergyAutoWidget(EnergyStorageAddon addon) {
        super(addon.getXPos(), addon.getYPos(), "Power");
        this.addon = addon;
    }

    public Pair<Integer, Integer> getAssetSizeXY() {
        return addon.getAssetSizeXY();
    }

    public Pair<Integer, Integer> getGuiXY() {
        return addon.getGuiXY();
    }

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenY = minecraft.getMainWindow().getScaledHeight()/2;
        int screenX = minecraft.getMainWindow().getScaledWidth()/2;
        float start = 1 - (float)addon.getEnergyStored()/addon.getMaxEnergyStored();
        float offset = (getAssetSizeXY().getValue() * start);
        addon.getAsset().drawPartial(screenX - getGuiXY().getKey()/2 + x,screenY - getGuiXY().getValue()/2 +y + offset,getAssetSizeXY().getKey(), getAssetSizeXY().getValue(),1,(float)addon.getEnergyStored()/addon.getMaxEnergyStored(),0,0);
    }

    @Override
    public void renderToolTip(int p_renderToolTip_1_, int p_renderToolTip_2_) {
        super.renderToolTip(p_renderToolTip_1_, p_renderToolTip_2_);
    }
}
