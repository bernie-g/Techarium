package software.bernie.techarium.display.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.interfaces.ITooltipAddon;
import software.bernie.techarium.machine.interfaces.ITooltipProvider;
import software.bernie.techarium.util.Vector2i;

public class EnergyAutoWidget extends DrawableWidget implements ITooltipProvider {

    private final EnergyStorageAddon addon;

    public EnergyAutoWidget(EnergyStorageAddon addon) {
        super(addon.getAsset(), addon.getPosX(), addon.getPosY(), 12, 48, new StringTextComponent("Power"));
        this.addon = addon;
    }

    public Vector2i getAssetSizeXY() {
        return addon.getSize();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        float relativeDrawHeight = Math.min(((float) addon.getEnergyStored())/addon.getMaxEnergyStored(), 1);
        float relativeDrawPositionY = 1 - relativeDrawHeight;
        int absoluteDrawHeight = (int) Math.ceil(relativeDrawHeight * getAssetSizeXY().getY());
        int absoluteDrawPositionY = (int) Math.floor(relativeDrawPositionY * getAssetSizeXY().getY());
        Vector2i size = new Vector2i(12, absoluteDrawHeight);
        Vector2i drawPos = new Vector2i(x,y + (int)(relativeDrawPositionY * getAssetSizeXY().getY()));
        Vector2i texturePos = addon.getAsset().getTexturePos().add(0, absoluteDrawPositionY);


        addon.getAsset().drawPartial(matrixStack, drawPos, size,texturePos);
    }

    @Override
    public ITooltipAddon getTooltip() {
        return addon;
    }
}
