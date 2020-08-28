package software.bernie.techariumbotanica.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.tesr.AnimatedBlockRenderer;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.client.render.model.WorkbenchModel;
import software.bernie.techariumbotanica.tile.TileWorkbench;

import java.awt.*;

public class WorkbenchTESR extends AnimatedBlockRenderer<TileWorkbench, WorkbenchModel>
{
	public WorkbenchTESR(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new WorkbenchModel());
	}

	@Override
	public ResourceLocation getBlockTexture(TileWorkbench tileWorkbench)
	{
		return new ResourceLocation(TechariumBotanica.ModID, "textures/block/workbench.png");
	}

	@Override
	protected Color getRenderColor(TileWorkbench tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		return new Color(255, 255, 255, 255);
	}
}
