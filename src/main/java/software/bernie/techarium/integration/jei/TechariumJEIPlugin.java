package software.bernie.techarium.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.integration.jei.panel.PanelBoundHandler;
import software.bernie.techarium.machine.screen.AutomaticContainerScreen;

@JeiPlugin
public class TechariumJEIPlugin implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return new ResourceLocation(Techarium.ModID, "techarium_jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{

	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
		registration.addGuiContainerHandler(AutomaticContainerScreen.class, new PanelBoundHandler());
	}
}
