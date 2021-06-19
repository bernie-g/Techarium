package software.bernie.techarium.display.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.display.container.ExchangeStationContainer;
import software.bernie.techarium.display.screen.widget.ScrollBarWidget;
import software.bernie.techarium.util.Vector2i;

public class ExchangeStationScreen extends AutomaticContainerScreen {
    //weird generic stuff, pls put only ExchangeStationStuff here
    public ExchangeStationScreen(AutomaticContainer container, PlayerInventory inv, ITextComponent containerName) {
        super(container, inv, containerName);
        if (! (container instanceof ExchangeStationContainer))
            throw new IllegalArgumentException("You should not pass a non ExchangeStationContainer into ExchangeStationScreen");

    }

    @Override
    protected void init() {
        super.init();
        addButton(new ScrollBarWidget(new Vector2i(61+leftPos,17+topPos),108, true));
    }

    @Override
    public ExchangeStationContainer getMenu() {
        return (ExchangeStationContainer) super.getMenu();
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        return super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_)
                | this.getFocused() != null && this.isDragging() && p_231045_5_ == 0 && this.getFocused().mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
    }
}
