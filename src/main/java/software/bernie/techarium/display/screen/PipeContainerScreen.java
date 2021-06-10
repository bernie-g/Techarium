package software.bernie.techarium.display.screen;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.client.screen.draw.UiTexture;
import software.bernie.techarium.display.container.PipeContainer;
import software.bernie.techarium.display.screen.widget.awt.*;
import software.bernie.techarium.display.screen.widget.pipe.BigInputOutput;
import software.bernie.techarium.display.screen.widget.pipe.FilterInputOutput;
import software.bernie.techarium.display.screen.widget.pipe.RedstoneControlWidget;
import software.bernie.techarium.pipe.util.RedstoneControlType;

import static software.bernie.techarium.Techarium.ModID;

public class PipeContainerScreen extends DrawableContainerScreen<PipeContainer> {

    private static final IDrawable BACKGROUND_TEXTURE = new UiTexture(new ResourceLocation(ModID, "textures/gui/pipe/inventory.png"), 201, 195).getFullArea();

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private boolean isInput = true;
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private RedstoneControlType activeRedstoneControlType = RedstoneControlType.ALWAYS_DISABLED;

    public PipeContainerScreen(PipeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        imageWidth = 201;
        imageHeight = 195;
    }


    @Override
    protected void init() {
        super.init();
        addButton(new BigInputOutput(new Point(0 + leftPos,80 + topPos), this::isInput, this::setInput));
        addButton(new FilterInputOutput(new Point(123 + leftPos,7 + topPos), this::isInput, this::setInput));
        addButton(new RedstoneControlWidget(new Point(17 + leftPos,82 + topPos), this::getActiveRedstoneControlType, this::setActiveRedstoneControlType));
    }

    @Override
    protected IDrawable getBackground() {
        return BACKGROUND_TEXTURE;
    }
}
