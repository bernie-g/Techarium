package software.bernie.techarium.display.screen.widget.pipe;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.display.screen.widget.FunctionImageButton;
import software.bernie.techarium.display.screen.widget.awt.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static software.bernie.techarium.Techarium.ModID;

public class BigInputOutput extends Widget {

    private static final ResourceLocation MACHINE_COMPONENTS = new ResourceLocation(ModID, "textures/gui/machine_components.png");
    private final Supplier<Boolean> isInput;
    private final Consumer<Boolean> setInput;


    private static final Point offsetInput = new Point(0,0);
    private static final Point offsetOutput = new Point(0,13);
    private final FunctionImageButton inputButton, outputButton;



    public BigInputOutput(Point position, Dimension size, Supplier<Boolean> isInput, Consumer<Boolean> setInput){
        super(position.x, position.y, size.width, size.height, StringTextComponent.EMPTY);
        this.isInput = isInput;
        this.setInput = setInput;
        this.inputButton = new FunctionImageButton(position, new Dimension(13,12), new Point(238,1), 13,MACHINE_COMPONENTS, ()-> setInput.accept(true));
        this.outputButton = new FunctionImageButton(position, new Dimension(13,12), new Point(238,27), 13,MACHINE_COMPONENTS,()-> setInput.accept(false));
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.pushPose();
        if (Boolean.FALSE.equals(isInput.get())) {
            matrixStack.translate(offsetInput.x, offsetInput.y, 0);
            inputButton.render(matrixStack, mouseX - offsetInput.x, mouseY - offsetInput.y, partialTicks);
        } else {
            matrixStack.translate(offsetOutput.x, offsetOutput.y, 0);
            outputButton.render(matrixStack, mouseX - offsetOutput.x, mouseY - offsetOutput.y, partialTicks);
        }
        matrixStack.popPose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (Boolean.FALSE.equals(isInput.get())) {
            return inputButton.mouseClicked(mouseX - offsetInput.x, mouseY - offsetInput.y, button);
        } else {
            return outputButton.mouseClicked(mouseX - offsetOutput.x, mouseY - offsetOutput.y, button);
        }
    }
}
