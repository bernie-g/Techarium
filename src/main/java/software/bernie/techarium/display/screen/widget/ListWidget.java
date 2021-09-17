package software.bernie.techarium.display.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techarium.display.screen.widget.ScrollBarWidget;
import software.bernie.techarium.display.screen.widget.SelectableWidget;
import software.bernie.techarium.util.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class ListWidget<T extends SelectableWidget> extends Widget {
    final List<T> widgets;
    final int numWidgets;
    final ScrollBarWidget scrollBar;
    final int firstY;
    final int yOff;

    private final Screen on;

    public ListWidget(Vector2i pos, Vector2i size, Vector2i scrollBarPos, int scrollBarLength, List<T> widgets, int numWidgets, int firstY, int yOff, Screen on) {
        super(pos.getX(), pos.getY(), size.getX(), size.getY(), StringTextComponent.EMPTY);
        scrollBar = new ScrollBarWidget(scrollBarPos.add(pos), scrollBarLength, true, false);
        this.widgets = widgets;
        this.numWidgets = numWidgets;
        this.yOff = yOff;
        this.firstY = firstY;
        widgets.forEach(selectable -> selectable.setListManager(this));
        this.on = on;
        widgets.get(0).setSelected(true);
    }

    public List<Widget> getRekursiveSubWidgets() {
        List<Widget> returnList = new ArrayList<>(widgets);
        returnList.add(scrollBar);
        return returnList;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int topVisibleWidget = topVisibleWidget();
        for (int i = 0; i < widgets.size(); i++) {
            Widget w = widgets.get(i);
            w.visible = i >= topVisibleWidget && i < topVisibleWidget + numWidgets;
            w.y = firstY + y + (i - topVisibleWidget) * yOff;
        }
    }

    private int topVisibleWidget() {
        if (widgets.size() <= numWidgets)
            return 0;
        return Math.min((int)(scrollBar.getScrollPosition()/(1f/(widgets.size()-numWidgets+1))),widgets.size() - numWidgets);
    }

    public void elementClicked(T widget) {
        if (!widgets.contains(widget))
            return;
        for (int i = 0; i < widgets.size(); i++) {
            T localWidget = widgets.get(i);
            if (localWidget == widget) {
                localWidget.setSelected(true);
                int finalI = i;
                localWidget.onClick.ifPresent(onClick -> onClick.accept(finalI, on));
            } else {
                localWidget.setSelected(false);
            }
        }
    }

    public void select(int index) {
        for (int i = 0; i < widgets.size(); i++) {
            T localWidget = widgets.get(i);
            if (i == index) {
                localWidget.setSelected(true);
                int finalI = i;
                localWidget.onClick.ifPresent(onClick -> onClick.accept(finalI, on));
            } else {
                localWidget.setSelected(false);
            }
        }
    }

    public T getSelected() {
        for (T widget : widgets) {
            if (widget.isSelected())
                return widget;
        }
        return null;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return scrollBar.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        return false;
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        return false;
    }

    @Override
    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        return false;
    }
}
