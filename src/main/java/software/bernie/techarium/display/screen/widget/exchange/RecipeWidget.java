package software.bernie.techarium.display.screen.widget.exchange;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.display.screen.widget.SelectableWidget;
import software.bernie.techarium.util.Vector2i;

import static software.bernie.techarium.Techarium.MOD_ID;

public class RecipeWidget extends SelectableWidget {
    private static final Vector2i SIZE = new Vector2i(52,18);
    private static final Vector2i TEXTURE_POSITION = new Vector2i(110,0);
    private static final ResourceLocation MACHINE_COMPONENTS = new ResourceLocation(MOD_ID, "textures/gui/machine_components.png");
    ItemStack input, output;
    public RecipeWidget(Vector2i pos, ItemStack input, ItemStack output) {
        super(pos, SIZE);
        this.input = input;
        this.output = output;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(MACHINE_COMPONENTS);
        if (isSelected) {
            blit(matrixStack, x, y, TEXTURE_POSITION.getX(), TEXTURE_POSITION.getY() + SIZE.getY(), SIZE.getX(), SIZE.getY());
        } else {
            blit(matrixStack, x, y, TEXTURE_POSITION.getX(), TEXTURE_POSITION.getY(), SIZE.getX(), SIZE.getY());
        }


        int size = input.getCount();
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(Minecraft.getInstance().player, input, x + 1, y + 1);
        Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font, input, x + 1, y + 1, size != 1 ? size + "" : "");

        size = output.getCount();
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(Minecraft.getInstance().player, output, x + 35, y + 1);
        Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font, output, x + 35, y + 1, size != 1 ? size + "" : "");
    }
}
