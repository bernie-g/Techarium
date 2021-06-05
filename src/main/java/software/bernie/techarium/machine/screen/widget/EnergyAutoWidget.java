package software.bernie.techarium.machine.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;

public class EnergyAutoWidget extends Widget {

    private final EnergyStorageAddon addon;

    public EnergyAutoWidget(EnergyStorageAddon addon) {
        super(addon.getPosX(), addon.getPosY(), 200, 20, new StringTextComponent("Power"));
        this.addon = addon;
    }

    public Pair<Integer, Integer> getAssetSizeXY() {
        return addon.getAssetSizeXY();
    }

    public Pair<Integer, Integer> getGuiXY() {
        return addon.getGuiXY();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenY = minecraft.getWindow().getGuiScaledHeight() / 2;
        int screenX = minecraft.getWindow().getGuiScaledWidth() / 2;

        float drawHeight = 1- ((float) addon.getEnergyStored()) / addon.getMaxEnergyStored();

        addon.getAsset().drawPartial(screenX - getGuiXY().getKey() / 2 + x,
                screenY - getGuiXY().getValue() / 2 + y, getAssetSizeXY().getKey(),
                getAssetSizeXY().getValue(), 1, 1, 0, drawHeight);
    }

}
