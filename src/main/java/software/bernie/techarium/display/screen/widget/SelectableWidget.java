package software.bernie.techarium.display.screen.widget;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.util.Vector2i;

import java.util.Optional;
import java.util.function.BiConsumer;

public class SelectableWidget extends Widget {

    @Setter
    ListWidget listManager;
    @Setter(AccessLevel.PACKAGE)
    @Getter
    protected boolean isSelected;
    public Optional<BiConsumer<Integer, Screen>> onClick = Optional.empty();
    public SelectableWidget(Vector2i pos, Vector2i size) {
        super(pos.getX(), pos.getY(), size.getX(), size.getY(), StringTextComponent.EMPTY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        listManager.elementClicked(this);
    }
}
