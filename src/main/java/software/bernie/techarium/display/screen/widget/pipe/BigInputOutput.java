package software.bernie.techarium.display.screen.widget.pipe;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.display.screen.widget.FunctionImageButton;
import software.bernie.techarium.util.Vector2i;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static software.bernie.techarium.Techarium.ModID;

public class BigInputOutput extends Widget {

    private static final ResourceLocation MACHINE_COMPONENTS = new ResourceLocation(ModID, "textures/gui/machine_components.png");
    private final Supplier<Boolean> isInput;
    private static final Vector2i size = new Vector2i(13,25);

    private static final Vector2i offsetInput = Vector2i.ZERO;
    private static final Vector2i offsetOutput = new Vector2i(0,13);
    private final FunctionImageButton inputButton, outputButton;



    public BigInputOutput(Vector2i position, Supplier<Boolean> isInput, Consumer<Boolean> setInput){
        super(position.getX(), position.getY(), size.getX(), size.getY(), StringTextComponent.EMPTY);
        this.isInput = isInput;
        this.inputButton = new FunctionImageButton(position.add(offsetInput), new Vector2i(13,12), new Vector2i(238,1), 13,MACHINE_COMPONENTS, ()-> setInput.accept(true));
        this.outputButton = new FunctionImageButton(position.add(offsetOutput), new Vector2i(13,12), new Vector2i(238,27), 13,MACHINE_COMPONENTS,()-> setInput.accept(false));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
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
