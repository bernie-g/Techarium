package software.bernie.techarium.display.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.client.screen.draw.UiTexture;
import software.bernie.techarium.display.container.PipeContainer;
import software.bernie.techarium.display.screen.widget.pipe.BigInputOutputWidget;
import software.bernie.techarium.display.screen.widget.pipe.FilterInputOutputWidget;
import software.bernie.techarium.display.screen.widget.pipe.MainConfigWidget;
import software.bernie.techarium.display.screen.widget.pipe.RedstoneControlWidget;
import software.bernie.techarium.network.container.ChangedMainConfigContainerPacket;
import software.bernie.techarium.network.container.ChangedRedstoneControlTypeContainerPacket;
import software.bernie.techarium.network.NetworkConnection;
import software.bernie.techarium.pipe.util.RedstoneControlType;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.util.Vector2i;

import static software.bernie.techarium.Techarium.MOD_ID;

public class PipeContainerScreen extends DrawableContainerScreen<PipeContainer> {

    private static final IDrawable BACKGROUND_TEXTURE = new UiTexture(new ResourceLocation(MOD_ID, "textures/gui/pipe/inventory.png"), 201, 195).getFullArea();

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
        addButton(new BigInputOutputWidget(new Vector2i(0 + leftPos,80 + topPos), this::isInput, this::setInput));
        addButton(new FilterInputOutputWidget(new Vector2i(123 + leftPos,7 + topPos), this::isInput, this::setInput));

        getMenu().getPipeTile().ifPresent( pipeTile -> {
            addButton(new RedstoneControlWidget(new Vector2i(17 + leftPos,82 + topPos),
                    () -> pipeTile.getConfig().getConfigBy(isInput).get(getMenu().tilePos.getDirection()).getRedstoneControlType(),
                    redstoneControlType -> NetworkConnection.INSTANCE.sendToServer(new ChangedRedstoneControlTypeContainerPacket(getMenu(), redstoneControlType, isInput))
            ));
            addButton(new MainConfigWidget(new Vector2i(14 + leftPos,7 + topPos),
                    () -> pipeTile.getConfig().getMainConfig().get(getMenu().tilePos.getDirection()),
                    config -> NetworkConnection.INSTANCE.sendToServer(new ChangedMainConfigContainerPacket(getMenu(),config))));
        });
    }

    @Override
    protected void renderCustomToolTips(MatrixStack matrixStack, int mouseX, int mouseY, int xCenter, int yCenter) {
        super.renderCustomToolTips(matrixStack, mouseX, mouseY, xCenter, yCenter);
        font.draw(matrixStack, LangRegistry.guiPipeInput.get(), 38,13, 0x1D2B33);
        font.draw(matrixStack, LangRegistry.guiPipeOutput.get(), 38,31, 0x1D2B33);
        font.draw(matrixStack, LangRegistry.guiPipeRoundRobin.get(), 38,49, 0x1D2B33);
        font.draw(matrixStack, LangRegistry.guiPipeSelfFeed.get(), 38,67, 0x1D2B33);
    }

    @Override
    protected IDrawable getBackground() {
        return BACKGROUND_TEXTURE;
    }
}
