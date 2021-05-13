package software.bernie.techarium.machine.addon.fluid;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.machine.interfaces.IToolTippedAddon;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.util.Utils;
import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.DEFAULT_FLUID_TANK;

public class FluidTankAddon extends FluidTank implements IContainerComponentProvider, IToolTippedAddon {

    private final String name;

    private final int posX;

    private final int posY;

    private final MachineMasterTile tile;

    private TankType tankType;

    private IDrawable tankDrawable;

    private int sizeX = 14;

    private int sizeY = 50;

    private int topOffset = 1;

    private int bottomOffset = 1;

    private int leftOffset = 1;

    private int rightOffset = 1;

    private Runnable onContentChange;

    public FluidTankAddon(MachineMasterTile tile, String name, int capacity, int posX, int posY, Predicate<FluidStack> validator) {
        super(capacity, validator);
        this.tile = tile;
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.tankType = TankType.FILL_AND_DRAIN;
        this.onContentChange = () -> {
        };
    }

    public void setTankDrawable(IDrawable tankDrawable, int sizeX, int sizeY, int topOffset, int bottomOffset,
                                int leftOffset, int rightOffset) {
        this.tankDrawable = tankDrawable;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.topOffset = topOffset;
        this.bottomOffset = bottomOffset;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        this.onContentChange.run();
    }

    public void setTankType(TankType tankType) {
        this.tankType = tankType;
    }


    public TankType getTankType() {
        return tankType;
    }

    public FluidTankAddon setOnContentChange(Runnable onContentChange) {
        this.onContentChange = onContentChange;
        return this;
    }

    public Runnable onContentChange() {
        return onContentChange;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getTopOffset() {
        return topOffset;
    }

    public int getRightOffset() {
        return rightOffset;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public int getBottomOffset() {
        return bottomOffset;
    }

    public IDrawable getTankDrawable() {
        if (tankDrawable == null) {
            return DEFAULT_FLUID_TANK;
        }
        return tankDrawable;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }

    public String getName() {
        return name;
    }

    public int fill(FluidStack resource, FluidAction action) {
        return this.getTankType().canBeFilled() ? super.fill(resource, action) : 0;
    }

    @Nonnull
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return this.getTankType().canBeDrained() ? this.drainInternal(resource, action) : FluidStack.EMPTY;
    }

    private FluidStack drainInternal(FluidStack resource, FluidAction action) {
        return !resource.isEmpty() && resource.isFluidEqual(this.fluid)
                ? this.drain(resource.getAmount(), action)
                : FluidStack.EMPTY;
    }

    @Nonnull
    public FluidStack drain(int maxDrain, FluidAction action) {
        return this.getTankType().canBeDrained() ? this.drainInternal(maxDrain, action) : FluidStack.EMPTY;
    }

    @Nonnull
    private FluidStack drainInternal(int maxDrain, FluidAction action) {
        int drained = maxDrain;
        if (this.fluid.getAmount() < maxDrain) {
            drained = this.fluid.getAmount();
        }

        FluidStack stack = new FluidStack(this.fluid, drained);
        if (action.execute() && drained > 0) {
            this.fluid.shrink(drained);
            this.onContentsChanged();
        }

        return stack;
    }

    public int fillForced(FluidStack resource, FluidAction action) {
        return super.fill(resource, action);
    }

    @Nonnull
    public FluidStack drainForced(FluidStack resource, FluidAction action) {
        return !resource.isEmpty() && resource.isFluidEqual(this.fluid)
                ? this.drainForced(resource.getAmount(), action)
                : FluidStack.EMPTY;
    }

    @Nonnull
    public FluidStack drainForced(int maxDrain, FluidAction action) {
        return this.drainInternal(maxDrain, action);
    }

    public Pair<Integer, Integer> getBackgroundSizeXY() {
        return tile.getController().getBackgroundSizeXY();
    }

    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        return new ArrayList<>();
    }

    @Override
    public void renderToolTip(Screen screen, int x, int y, int xCenter, int yCenter, int mouseX, int mouseY) {
        if (mouseX >= x + getPosX() && mouseX <= x + getPosX() + sizeX) {
            if (mouseY >= y + getPosY() && mouseY <= y + getPosY() + sizeY) {
                if (this.getFluid().isEmpty()) {
                    TextComponent component = Component.text("Empty", NamedTextColor.GOLD);
                    screen.renderTooltip(new MatrixStack(), Utils.wrapText(component), mouseX - xCenter, mouseY - yCenter);
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat();
                    TextComponent fluidComponent = Component.text("Fluid: ", NamedTextColor.GOLD)
                            .append(Component.text(getFluidName(fluid)));

                    TextComponent component = Component.text("Power: ", NamedTextColor.GOLD)
                            .append(Component.text(decimalFormat.format(this.getFluidAmount()), NamedTextColor.GOLD))
                            .append(Component.text("/", NamedTextColor.WHITE))
                            .append(Component.text(decimalFormat.format(this.getCapacity()), NamedTextColor.GOLD))
                            .append(Component.text(" MB", NamedTextColor.DARK_AQUA));

                    screen.func_243308_b(new MatrixStack(), Utils.wrapText(fluidComponent, component), mouseX - xCenter, mouseY - yCenter);
                }
            }
        }
    }

    public static String getFluidName(FluidStack stack)
    {
        return new TranslationTextComponent(stack.getFluid().getAttributes().getTranslationKey(stack)).getString();
    }

}
