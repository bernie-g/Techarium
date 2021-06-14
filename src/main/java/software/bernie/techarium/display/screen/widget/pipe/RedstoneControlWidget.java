package software.bernie.techarium.display.screen.widget.pipe;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.display.screen.widget.FunctionImageButton;
import software.bernie.techarium.pipe.util.RedstoneControlType;
import software.bernie.techarium.util.Vector2i;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static software.bernie.techarium.Techarium.ModID;

public class RedstoneControlWidget extends Widget {

    private static final ResourceLocation MACHINE_COMPONENTS = new ResourceLocation(ModID, "textures/gui/machine_components.png");

    private static final Vector2i size = new Vector2i(68,22);
    final Map<RedstoneControlType, Widget> redstoneActiveControlWidgets = new EnumMap<>(RedstoneControlType.class);
    final Map<RedstoneControlType, Widget> redstoneInactiveControlWidgets = new EnumMap<>(RedstoneControlType.class);
    final Supplier<RedstoneControlType> getRedstoneControlType;
    final Consumer<RedstoneControlType> setRedstoneControlType;

    public RedstoneControlWidget(Vector2i position, Supplier<RedstoneControlType> getRedstoneControlType, Consumer<RedstoneControlType> setRedstoneControlType) {
        super (position.getX(), position.getY(), size.getX(), size.getY(), StringTextComponent.EMPTY);
        this.getRedstoneControlType = getRedstoneControlType;
        this.setRedstoneControlType = setRedstoneControlType;
        for (RedstoneControlType redstoneControlType: RedstoneControlType.values()) {
            redstoneActiveControlWidgets.put(redstoneControlType, new ImageButton(position.getX() + 1 + 18*redstoneControlType.ordinal(), position.getY() + 1, 12, 13, 163 + 12*redstoneControlType.ordinal(),1,0, MACHINE_COMPONENTS, button -> {}));
            redstoneActiveControlWidgets.get(redstoneControlType).active = false;
            redstoneInactiveControlWidgets.put(redstoneControlType, new FunctionImageButton(position.add(new Vector2i(1 + 18 * redstoneControlType.ordinal(), 8)), new Vector2i(12, 13), new Vector2i(163 + 12 * redstoneControlType.ordinal(), 14), 13, MACHINE_COMPONENTS, () -> setRedstoneControlType.accept(redstoneControlType)));
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
        return false;
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
