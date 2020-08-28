package software.bernie.techariumbotanica.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.container.BotariumContainer;


public class BotariumScreen extends ContainerScreen<BotariumContainer>
{
	public static ResourceLocation GUI_TEXTURE = new ResourceLocation(TechariumBotanica.ModID, "textures/gui/container/gui_botarium_tier_1.png");

	public BotariumScreen(BotariumContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;

		AbstractGui.blit(relX, relY, 173, 184, 0, 0, 173, 184, 224, 184);
	}
}
