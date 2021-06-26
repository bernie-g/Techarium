package software.bernie.techarium.display.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.display.container.ExchangeStationContainer;
import software.bernie.techarium.display.screen.widget.SelectableWidget;
import software.bernie.techarium.display.screen.widget.ListWidget;
import software.bernie.techarium.display.screen.widget.exchange.RecipeWidget;
import software.bernie.techarium.network.NetworkConnection;
import software.bernie.techarium.network.container.RecipeWidgetClickContainerPacket;
import software.bernie.techarium.recipe.recipe.ExchangeStationRecipe;
import software.bernie.techarium.util.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static software.bernie.techarium.Techarium.ModID;

public class ExchangeStationScreen extends AutomaticContainerScreen {

    private static final ResourceLocation MACHINE_COMPONENTS = new ResourceLocation(ModID, "textures/gui/machine_components.png");
    private List<ExchangeStationRecipe> recipes;

    private final ExchangeStationContainer exchangeStationContainer;

    //weird generic stuff, pls put only ExchangeStationStuff here
    public ExchangeStationScreen(AutomaticContainer container, PlayerInventory inv, ITextComponent containerName) {
        super(container, inv, containerName);
        if (! (container instanceof ExchangeStationContainer))
            throw new IllegalArgumentException("You should not pass a non ExchangeStationContainer into ExchangeStationScreen");
        exchangeStationContainer = (ExchangeStationContainer) container;
        recipes = exchangeStationContainer.getMachineController().getRecipes().collect(Collectors.toList());
    }

    @Override
    protected void init() {
        super.init();
        List<SelectableWidget> widgets = new ArrayList<>();
        for (int i = 0; i < recipes.size(); i++) {
            ExchangeStationRecipe recipe = recipes.get(i);
            RecipeWidget recipeWidget = new RecipeWidget(new Vector2i(16 + leftPos, 24 + topPos + 18*i), recipe.getInput(), recipe.getOutput());
            recipeWidget.onClick = Optional.of((widget, screen) -> NetworkConnection.INSTANCE.sendToServer(new RecipeWidgetClickContainerPacket(getMenu(), widget)));
            widgets.add(recipeWidget);
        }
        ListWidget widget = new ListWidget(new Vector2i(15 + leftPos,23 + topPos), new Vector2i(62,110),new Vector2i(54,0), 110, widgets, 6, 1, 18, this);
        addButton(widget);
        widget.getRekursiveSubWidgets().forEach(this::addButton);
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
