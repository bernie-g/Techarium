package software.bernie.techarium.machine.addon.fluid;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import software.bernie.techarium.client.ClientUtils;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.addon.ExposeType;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.machine.interfaces.ITooltipAddon;
import software.bernie.techarium.util.Utils;
import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import software.bernie.techarium.util.Vector2i;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.DEFAULT_FLUID_TANK;

@Accessors(chain = true)
public class FluidTankAddon extends FluidTank implements IContainerComponentProvider, ITooltipAddon {

    private final String name;
    private final int posX;
    private final int posY;
    private final Vector2i backgroundSizeXY;
    private IDrawable tankDrawable;
    private Vector2i size = new Vector2i(14,50);
    private int topOffset = 1;
    private int bottomOffset = 1;
    private int leftOffset = 1;
    private int rightOffset = 1;
    private Runnable onContentChange;

    @Getter
    @Setter
    private ExposeType exposeType = ExposeType.BOTH;

    public FluidTankAddon(Vector2i backgroundSizeXY, String name, int capacity, int posX, int posY, Predicate<FluidStack> validator) {
        super(capacity, validator);
        this.backgroundSizeXY = backgroundSizeXY;
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.onContentChange = () -> {
        };
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        this.onContentChange.run();
    }

    public FluidTankAddon setOnContentChange(Runnable onContentChange) {
        this.onContentChange = onContentChange;
        return this;
    }

    public Runnable onContentChange() {
        return onContentChange;
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

    @Override
    public Vector2i getSize() {
        return size;
    }

    public int getPosX() {
        return posX;
    }

    public String getName() {
        return name;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return exposeType.canInsert() ? super.fill(resource, action) : 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return exposeType.canExtract() ? this.drainInternal(resource, action) : FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return exposeType.canExtract() ? this.drainInternal(maxDrain, action) : FluidStack.EMPTY;
    }

    public FluidStack drainInternal(FluidStack resource, FluidAction action) {
        return !resource.isEmpty() && resource.isFluidEqual(this.fluid)
                ? this.drain(resource.getAmount(), action)
                : FluidStack.EMPTY;
    }

    @Nonnull
    private FluidStack drainInternal(int maxDrain, FluidAction action) {

        int toDrain = Math.min(fluid.getAmount(), maxDrain);

        FluidStack drained = new FluidStack(this.fluid, toDrain);
        if (action.execute() && toDrain > 0) {
            this.fluid.shrink(toDrain);
            this.onContentsChanged();
        }
        return drained;
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

    public Vector2i getBackgroundSizeXY() {
        return backgroundSizeXY;
    }

    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        return new ArrayList<>();
    }

    @Override
    public List<ITextComponent> createToolTipMessage() {
        if (this.getFluid().isEmpty()) {
            return Utils.wrapMultipleText(Component.text("Empty", NamedTextColor.GOLD));
        } else {
            DecimalFormat decimalFormat = new DecimalFormat();
            TextComponent fluidComponent = Component.text("Fluid: ", NamedTextColor.GOLD)
                    .append(Component.text(getFluidName(fluid)));
            TextComponent component = Component.text(decimalFormat.format(this.getFluidAmount()), NamedTextColor.GOLD)
                    .append(Component.text("/", NamedTextColor.WHITE))
                    .append(Component.text(decimalFormat.format(this.getCapacity()), NamedTextColor.GOLD))
                    .append(Component.text(" MB", NamedTextColor.DARK_AQUA));
            if (ClientUtils.isAdvancedItem()) {
                TextComponent registryName = Component.text(fluid.getFluid().getRegistryName().toString());
                return Utils.wrapMultipleText(fluidComponent, component, registryName);
            } else {
                return Utils.wrapMultipleText(fluidComponent, component);
            }
        }
    }
    public static String getFluidName(FluidStack stack) {
        return new TranslationTextComponent(stack.getFluid().getAttributes().getTranslationKey(stack)).getString();
    }
}
