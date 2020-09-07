package software.bernie.techarium.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Random;

public class RenderUtils
{
	public static Runnable renderModel(MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, IBakedModel model, Random rand, int combinedLightIn, int combinedOverlayIn)
	{
		return () -> Minecraft.getInstance().getItemRenderer().renderQuads(matrixStack, iRenderTypeBuffer.getBuffer(
				RenderType.getTranslucent()), model.getQuads(null, null, rand, null), new ItemStack(
				Items.DIRT), combinedLightIn, combinedOverlayIn);
	}
}
