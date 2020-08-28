package software.bernie.techariumbotanica.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.block.ArboratoriumBase;
import software.bernie.techariumbotanica.client.RenderUtils;
import software.bernie.techariumbotanica.tile.TileArboratorium;

public class RenderArboratorium extends TileEntityRenderer
{
	public RenderArboratorium(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileEntity tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
	{
		if (tile != null && tile instanceof TileArboratorium)
		{
			ModelManager modelManager = Minecraft.getInstance().getModelManager();

			World world = tile.getWorld();
			BlockState state = world.getBlockState(tile.getPos());
			ArboratoriumBase block = (ArboratoriumBase) state.getBlock();
			IBakedModel model = modelManager.getModel(new ResourceLocation(TechariumBotanica.ModID + ":block/arboratorium"));

			stack.push();
			stack.translate(0.5, 0.5, 0.5);

			if (state.has(ArboratoriumBase.FACING))
			{
				Direction dir = state.get(ArboratoriumBase.FACING);
				if (dir == Direction.EAST)
				{
					stack.rotate(new Quaternion(0, -90, 0, true));
				}
				if (dir == Direction.SOUTH)
				{
					stack.rotate(new Quaternion(0, 180, 0, true));
				}
				if (dir == Direction.WEST)
				{
					stack.rotate(new Quaternion(0, 90, 0, true));
				}
			}
			stack.translate(-0.5, -0.5, -0.5);
			RenderUtils.renderModel(stack, buffer, model, world.getRandom(), combinedLightIn, combinedOverlayIn).run();
			stack.pop();
		}

	}
}
