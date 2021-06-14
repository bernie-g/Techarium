package software.bernie.techarium.display.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.util.Vector2i;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SwitchingButtonWidget extends Widget {

    private final Supplier<Boolean> getState;

    private final FunctionImageButton on, off;


    public SwitchingButtonWidget(Vector2i screenPos, Vector2i size, ResourceLocation texture, Vector2i texturePos1, int yOffset, Vector2i texturePos2, Supplier<Boolean> getState, Consumer<Boolean> onChanged) {
        super(screenPos.getX(), screenPos.getY(), size.getX(), size.getY(), StringTextComponent.EMPTY);
        this.getState = getState;
        on = new FunctionImageButton(screenPos, size, texturePos1, yOffset , texture, () -> onChanged.accept(false));
        off = new FunctionImageButton(screenPos, size, texturePos2, yOffset , texture, () -> onChanged.accept(true));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        on.visible = visible && getState.get();
        off.visible = visible && !getState.get();
        on.render(matrixStack, mouseX, mouseY, partialTicks);
        off.render(matrixStack, mouseX, mouseY, partialTicks);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return on.mouseClicked(mouseX, mouseY, button)
                || off.mouseClicked(mouseX, mouseY, button);

    }
}
