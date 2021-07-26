package software.bernie.techarium.display.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.client.screen.draw.GuiAddonTextures;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.display.container.AssemblerContainer;

public class AssemblerContainerScreen extends DrawableContainerScreen<AssemblerContainer> {

	public AssemblerContainerScreen(AssemblerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		imageHeight = 168;
		imageWidth = 177;
	}
	
	protected IDrawable getBackground() {
		return GuiAddonTextures.ASSEMBLER_DRAWABLE;
	}
}