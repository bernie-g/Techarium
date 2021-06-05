package software.bernie.techarium.machine.addon.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.interfaces.IToolTippedAddon;
import software.bernie.techarium.recipes.AbstractMachineRecipe;
import software.bernie.techarium.util.Utils;

import java.text.DecimalFormat;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.DEFAULT_ENERGY_BAR;

public class EnergyStorageAddon extends EnergyStorage implements INBTSerializable<CompoundNBT>, IToolTippedAddon {

    private final int posX;
    private final int posY;

    private MachineController<? extends AbstractMachineRecipe> controller;

    private IDrawable asset;

    private Pair<Integer, Integer> assetSizeXY;

    private Pair<Integer, Integer> guiXY;

    public EnergyStorageAddon(MachineController<? extends AbstractMachineRecipe> controller, int totalEnergy, int posX, int posY, Pair<Integer, Integer> guiXY) {
        this(controller, totalEnergy, totalEnergy, posX, posY, guiXY);
    }

    public EnergyStorageAddon(MachineController<? extends AbstractMachineRecipe> controller, int totalEnergy, int maxIO, int posX, int posY, Pair<Integer, Integer> guiXY) {
        this(controller, totalEnergy, maxIO, maxIO, posX, posY, guiXY);
    }

    public EnergyStorageAddon(MachineController<? extends AbstractMachineRecipe> controller, int totalEnergy, int maxIn, int maxOut, int posX, int posY,
                              Pair<Integer, Integer> guiXY) {
        super(totalEnergy, maxIn, maxOut);
        this.controller = controller;
        this.posY = posY;
        this.posX = posX;
        this.assetSizeXY = Pair.of(12, 48);
        this.guiXY = guiXY;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getSizeX() {
        return assetSizeXY.getLeft();
    }

    public int getSizeY() {
        return assetSizeXY.getRight();
    }

    public Pair<Integer, Integer> getAssetSizeXY() {
        return assetSizeXY;
    }

    public Pair<Integer, Integer> getGuiXY() {
        return guiXY;
    }

    public IDrawable getAsset() {
        if (asset == null) {
            asset = DEFAULT_ENERGY_BAR;
        }
        return asset;
    }

    public EnergyStorageAddon setAsset(IDrawable asset, int sizeX, int sizeY) {
        this.assetSizeXY = Pair.of(sizeX, sizeY);
        this.asset = asset;
        return this;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("energy", this.energy);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
    }

    @Override
    public void renderToolTip(Screen screen, int x, int y, int xCenter, int yCenter, int mouseX, int mouseY) {
        if (mouseX >= x + getPosX() && mouseX <= x + getPosX() + getAssetSizeXY().getKey()) {
            if (mouseY >= y + getPosY() && mouseY <= y + getPosY() + getAssetSizeXY().getValue()) {
                DecimalFormat decimalFormat = new DecimalFormat();
                TextComponent component = Component.text("Power: ", NamedTextColor.GOLD)
                        .append(Component.text(decimalFormat.format(getEnergyStored()), NamedTextColor.GOLD))
                        .append(Component.text("/", NamedTextColor.WHITE))
                        .append(Component.text(decimalFormat.format(getMaxEnergyStored()), NamedTextColor.GOLD))
                        .append(Component.text(" FE", NamedTextColor.DARK_AQUA));

                int energyCost = controller.getCurrentRecipe() == null || controller.getEnergyStorage().getEnergyStored() == 0 ? 0 : controller.getCurrentRecipe().getRfPerTick();
                TextComponent usage = Component.text("Using: ", NamedTextColor.GOLD)
                        .append(Component.text(
                                energyCost,
                                NamedTextColor.GOLD))
                        .append(Component.text(" FE/t", NamedTextColor.DARK_AQUA));
                screen.renderComponentTooltip(new MatrixStack(), Utils.wrapText(component, usage), mouseX - xCenter, mouseY - yCenter);
            }
        }
    }
}
