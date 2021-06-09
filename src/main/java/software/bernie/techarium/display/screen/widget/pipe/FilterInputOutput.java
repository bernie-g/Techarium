package software.bernie.techarium.display.screen.widget.pipe;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.display.screen.widget.FunctionImageButton;
import software.bernie.techarium.display.screen.widget.awt.Dimension;
import software.bernie.techarium.display.screen.widget.awt.Point;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static software.bernie.techarium.Techarium.ModID;

public class FilterInputOutput extends Widget {

    private static final ResourceLocation MACHINE_COMPONENTS = new ResourceLocation(ModID, "textures/gui/machine_components.png");
    private final Supplier<Boolean> isInput;
    private static final Dimension size = new Dimension(11,17);
    private static final Point offsetInput = new Point(0,0);
    private static final Point offsetOutput = new Point(0,9);
    private final FunctionImageButton inputButton, outputButton;



    public FilterInputOutput(Point position, Supplier<Boolean> isInput, Consumer<Boolean> setInput){
        super(position.x, position.y, size.width, size.height, StringTextComponent.EMPTY);
        this.isInput = isInput;
        this.inputButton = new FunctionImageButton(position.add(offsetInput), new Dimension(12,9), new Point(207,62), 9,MACHINE_COMPONENTS, ()-> setInput.accept(true));
        this.outputButton = new FunctionImageButton(position.add(offsetOutput), new Dimension(12,9), new Point(220,62), 9,MACHINE_COMPONENTS,()-> setInput.accept(false));
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        inputButton.visible = visible;
        outputButton.visible = visible;
        if (Boolean.FALSE.equals(isInput.get())) {
            inputButton.render(matrixStack, mouseX, mouseY, partialTicks);
        } else {
            outputButton.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!visible)
            return false;
        if (Boolean.FALSE.equals(isInput.get())) {
            return inputButton.mouseClicked(mouseX, mouseY, button);
        } else {
            return outputButton.mouseClicked(mouseX, mouseY, button);
        }
    }
}
