package software.bernie.techarium.display.screen;

import static software.bernie.techarium.Techarium.ModID;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.client.screen.draw.UiTexture;
import software.bernie.techarium.display.container.AssemblerContainer;

public class AssemblerContainerScreen extends DrawableContainerScreen<AssemblerContainer> {

    private static final IDrawable BACKGROUND_TEXTURE = new UiTexture(new ResourceLocation(ModID, "textures/gui/assembler/assembler.png"), 201, 195).getFullArea();
	
	public AssemblerContainerScreen(AssemblerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		imageHeight = 168;
		imageWidth = 177;
	}
	
	protected IDrawable getBackground() {
		return BACKGROUND_TEXTURE;
	}
}