package software.bernie.techarium.integration.jei;

import lombok.experimental.UtilityClass;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.techarium.Techarium;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Techarium.ModID, value = Dist.CLIENT)
public class JeiHandler {

    /**
     * Get's called after every screen draw. Needed because we need to draw our {@link RecipeDisplayWidget} after JEIs RecipeButtons
     * The invalid RecipeDisplayWidgets are removed from the screen and from their intermediate storage in {@link software.bernie.techarium.integration.jei.category.BaseRecipeCategory#recipeWidgets}
     */
    @SubscribeEvent
    public static void onDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
        List<RecipeDisplayWidget> toRemove = new ArrayList<>();
        event.getGui().buttons.stream().filter(RecipeDisplayWidget.class::isInstance).map(RecipeDisplayWidget.class::cast).forEach(widget -> {
            if (widget.renderButtonWithFeedback())
                toRemove.add(widget);
        });
        event.getGui().buttons.removeAll(toRemove);
        toRemove.forEach(widget -> widget.getRemoveFrom().inverse().remove(widget));
    }
}
