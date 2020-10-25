package software.bernie.techarium.machine.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;

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

}
