package software.bernie.techarium.display.screen.widget.pipe;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.display.screen.widget.FunctionImageButton;
import software.bernie.techarium.display.screen.widget.SwitchingButtonWidget;
import software.bernie.techarium.pipe.util.PipeMainConfig;
import software.bernie.techarium.util.Vector2i;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static software.bernie.techarium.Techarium.ModID;

public class MainConfigWidget extends Widget {

    private static final ResourceLocation MACHINE_COMPONENTS = new ResourceLocation(ModID, "textures/gui/machine_components.png");
    private static final Vector2i size = new Vector2i(20,73);

    private static final Vector2i firstOffset = new Vector2i(3,3);
    private static final Vector2i furtherOffsets = new Vector2i(0,18);

    private static final Vector2i textureOffsetRight = new Vector2i(15,0);
    private static final Vector2i textureOffsetDown = new Vector2i(0,60);
    private final SwitchingButtonWidget inputButton, outputButton, roundRobinButton, selfFeedButton;



    public MainConfigWidget(Vector2i position, Supplier<PipeMainConfig> getConfig, Consumer<PipeMainConfig> setConfig){
        super(position.getX(), position.getY(), size.getX(), size.getY(), StringTextComponent.EMPTY);
        inputButton = new SwitchingButtonWidget(position.add(firstOffset), new Vector2i(14,14), MACHINE_COMPONENTS, new Vector2i(226,117), 15, new Vector2i(226,147), getConfig.get()::isInput,bool -> setConfig.accept(getConfig.get().setInput(bool)));
        outputButton = new SwitchingButtonWidget(position.add(firstOffset).add(furtherOffsets), new Vector2i(14,14), MACHINE_COMPONENTS, new Vector2i(226,117).add(textureOffsetRight), 15, new Vector2i(226,147).add(textureOffsetRight), getConfig.get()::isOutput, bool -> setConfig.accept(getConfig.get().setOutput(bool)));
        roundRobinButton = new SwitchingButtonWidget(position.add(firstOffset).add(furtherOffsets).add(furtherOffsets), new Vector2i(14,14), MACHINE_COMPONENTS, new Vector2i(226,117).add(textureOffsetDown), 15, new Vector2i(226,147).add(textureOffsetDown), getConfig.get()::isRoundRobin, bool -> setConfig.accept(getConfig.get().setRoundRobin(bool)));
        selfFeedButton = new SwitchingButtonWidget(position.add(firstOffset).add(furtherOffsets).add(furtherOffsets).add(furtherOffsets), new Vector2i(14,14), MACHINE_COMPONENTS, new Vector2i(226,117).add(textureOffsetDown).add(textureOffsetRight), 15, new Vector2i(226,147).add(textureOffsetDown).add(textureOffsetRight), getConfig.get()::isSelfFeed, bool -> setConfig.accept(getConfig.get().setSelfFeed(bool)));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        inputButton.visible = visible;
        outputButton.visible = visible;
        roundRobinButton.visible = visible;
        selfFeedButton.visible = visible;
        inputButton.render(matrixStack, mouseX, mouseY, partialTicks);
        outputButton.render(matrixStack, mouseX, mouseY, partialTicks);
        roundRobinButton.render(matrixStack, mouseX, mouseY, partialTicks);
        selfFeedButton.render(matrixStack, mouseX, mouseY, partialTicks);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return inputButton.mouseClicked(mouseX, mouseY, button)
                || outputButton.mouseClicked(mouseX, mouseY, button)
                || roundRobinButton.mouseClicked(mouseX, mouseY, button)
                || selfFeedButton.mouseClicked(mouseX, mouseY, button);

    }
}
