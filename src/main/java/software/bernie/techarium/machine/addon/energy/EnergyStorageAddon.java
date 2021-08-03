package software.bernie.techarium.machine.addon.energy;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.interfaces.ITooltipAddon;
import software.bernie.techarium.util.Utils;
import software.bernie.techarium.util.Vector2i;
import java.text.DecimalFormat;
import java.util.List;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.DEFAULT_ENERGY_BAR;

public class EnergyStorageAddon extends EnergyStorage implements INBTSerializable<CompoundNBT>, ITooltipAddon {

    private final int posX;
    private final int posY;
    private IDrawable asset;
    private Vector2i size;

    @Getter
    @Setter
    private int lastDrained = 0;

    public EnergyStorageAddon(int totalEnergy, int posX, int posY) {
        this(totalEnergy, totalEnergy, posX, posY);
    }

    public void forceSetEnergy(int energy) {
        this.energy = energy;
    }

    public EnergyStorageAddon(int totalEnergy, int maxIO, int posX, int posY) {
        this(totalEnergy, maxIO, maxIO, posX, posY);
    }

    public EnergyStorageAddon(int totalEnergy, int maxIn, int maxOut, int posX, int posY) {
        super(totalEnergy, maxIn, maxOut);
        this.posY = posY;
        this.posX = posX;
        this.size = new Vector2i(12, 48);
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getSizeX() {
        return size.getX();
    }

    public int getSizeY() {
        return size.getY();
    }

    @Override
    public Vector2i getSize() {
        return size;
    }

    public IDrawable getAsset() {
        if (asset == null) {
            asset = DEFAULT_ENERGY_BAR;
        }
        return asset;
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
    public List<ITextComponent> createToolTipMessage() {
        DecimalFormat decimalFormat = new DecimalFormat();
        TextComponent component = Component.text("Power: ", NamedTextColor.GOLD)
                .append(Component.text(decimalFormat.format(getEnergyStored()), NamedTextColor.GOLD))
                .append(Component.text("/", NamedTextColor.WHITE))
                .append(Component.text(decimalFormat.format(getMaxEnergyStored()), NamedTextColor.GOLD))
                .append(Component.text(" FE", NamedTextColor.DARK_AQUA));

        int energyCost = getEnergyStored() == 0 ? 0 : lastDrained;
        TextComponent usage = Component.text("Using: ", NamedTextColor.GOLD)
                .append(Component.text(
                        energyCost,
                        NamedTextColor.GOLD))
                .append(Component.text(" FE/t", NamedTextColor.DARK_AQUA));
        return Utils.wrapMultipleText(component, usage);

    }
}
