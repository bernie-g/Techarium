package software.bernie.techarium.display.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.client.screen.draw.UiTexture;
import software.bernie.techarium.display.container.PipeContainer;

import static software.bernie.techarium.Techarium.ModID;

public class PipeContainerScreen extends DrawableContainerScreen<PipeContainer> {

    private static final IDrawable BACKGROUND_TEXTURE = new UiTexture(new ResourceLocation(ModID, "textures/gui/pipe/inventory.png"), 603, 585).getFullArea();

    public PipeContainerScreen(PipeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected IDrawable getBackground() {
        return BACKGROUND_TEXTURE;
    }
}
