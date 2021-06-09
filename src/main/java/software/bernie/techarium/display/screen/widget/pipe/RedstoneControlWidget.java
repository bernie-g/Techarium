package software.bernie.techarium.display.screen.widget.pipe;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.display.screen.widget.FunctionImageButton;
import software.bernie.techarium.display.screen.widget.awt.Dimension;
import software.bernie.techarium.display.screen.widget.awt.Point;
import software.bernie.techarium.pipe.capability.RedstoneControlType;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static software.bernie.techarium.Techarium.ModID;

public class RedstoneControlWidget extends Widget {

    private static final ResourceLocation MACHINE_COMPONENTS = new ResourceLocation(ModID, "textures/gui/machine_components.png");

    private static final Dimension size = new Dimension(68,22);
    final Map<RedstoneControlType, Widget> redstoneActiveControlWidgets = new EnumMap<>(RedstoneControlType.class);
    final Map<RedstoneControlType, Widget> redstoneInactiveControlWidgets = new EnumMap<>(RedstoneControlType.class);
    final Supplier<RedstoneControlType> getRedstoneControlType;
    final Consumer<RedstoneControlType> setRedstoneControlType;

    public RedstoneControlWidget(Point position, Supplier<RedstoneControlType> getRedstoneControlType, Consumer<RedstoneControlType> setRedstoneControlType) {
        super (position.x, position.y, size.width, size.height, StringTextComponent.EMPTY);
        this.getRedstoneControlType = getRedstoneControlType;
        this.setRedstoneControlType = setRedstoneControlType;
        for (RedstoneControlType redstoneControlType: RedstoneControlType.values()) {
            redstoneActiveControlWidgets.put(redstoneControlType, new ImageButton(position.x + 1 + 18*redstoneControlType.ordinal(), position.y + 1, 12, 13, 139 + 12*redstoneControlType.ordinal(),100,0, MACHINE_COMPONENTS, (button) -> {}));
            redstoneActiveControlWidgets.get(redstoneControlType).active = false;
            redstoneInactiveControlWidgets.put(redstoneControlType, new FunctionImageButton(position.add(new Point(1 + 18 * redstoneControlType.ordinal(), 8)), new Dimension(12, 13), new Point(139 + 12 * redstoneControlType.ordinal(), 113), 13, MACHINE_COMPONENTS, () -> setRedstoneControlType.accept(redstoneControlType)));
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!visible)
            return false;
        for (RedstoneControlType redstoneControlType: RedstoneControlType.values()) {
            if (redstoneInactiveControlWidgets.get(redstoneControlType).mouseClicked(mouseX, mouseY, button))
                return true;
        }
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RedstoneControlType activeRedstoneControlType = getRedstoneControlType.get();
        for (RedstoneControlType redstoneControlType: RedstoneControlType.values()) {
            redstoneActiveControlWidgets.get(redstoneControlType).visible = activeRedstoneControlType == redstoneControlType;
            redstoneInactiveControlWidgets.get(redstoneControlType).visible = activeRedstoneControlType != redstoneControlType;
            redstoneActiveControlWidgets.get(redstoneControlType).render(matrixStack, mouseX, mouseY, partialTicks);
            redstoneInactiveControlWidgets.get(redstoneControlType).render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }
}
