package software.bernie.techariumbotanica.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
import software.bernie.techariumbotanica.block.BotariumBase;
import software.bernie.techariumbotanica.client.RenderUtils;
import software.bernie.techariumbotanica.registry.BlockRegistry;
import software.bernie.techariumbotanica.registry.ModelRegistry;
import software.bernie.techariumbotanica.tile.TileBotarium;

public class RenderBotarium extends TileEntityRenderer
{
	public RenderBotarium(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileEntity tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
	{
		if(tile != null && tile instanceof TileBotarium)
		{
			ModelManager modelManager = Minecraft.getInstance().getModelManager();

			World world = tile.getWorld();
			BlockState state = world.getBlockState(tile.getPos());
			BotariumBase block = (BotariumBase) state.getBlock();
			IBakedModel model;
			switch(block.TIER)
			{
				case 1:
					model = modelManager.getModel(
							new ResourceLocation(TechariumBotanica.ModID + ":block/botarium_tier_1"));
					break;
				case 2:
					model = modelManager.getModel(
							new ResourceLocation(TechariumBotanica.ModID + ":block/botarium_tier_2"));
					break;
				case 3:
					model = modelManager.getModel(
							new ResourceLocation(TechariumBotanica.ModID + ":block/botarium_tier_3"));
					break;
				case 4:
					model = modelManager.getModel(
							new ResourceLocation(TechariumBotanica.ModID + ":block/botarium_tier_4"));
					break;
				case 5:
					model = modelManager.getModel(
							new ResourceLocation(TechariumBotanica.ModID + ":block/botarium_tier_5"));
					break;
				default:
					model = modelManager.getModel(
							new ResourceLocation(TechariumBotanica.ModID + ":block/botarium_tier_1"));
					break;
			}

			stack.push();
			stack.translate(0.5, 0.5, 0.5);

			if(state.has(BotariumBase.FACING))
			{
				Direction dir = state.get(BotariumBase.FACING);
				if(dir == Direction.EAST)
				{
					stack.rotate(new Quaternion(0, -90, 0, true));
				}
				if(dir == Direction.SOUTH)
				{
					stack.rotate(new Quaternion(0, 180, 0, true));
				}
				if(dir == Direction.WEST)
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
