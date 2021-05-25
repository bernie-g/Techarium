package software.bernie.techarium.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.item.MachineItem;
import software.bernie.techarium.util.BlockRegion;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Techarium.ModID, value = Dist.CLIENT)
public class RenderUtils
{
	public static Runnable renderModel(MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, IBakedModel model, Random rand, int combinedLightIn, int combinedOverlayIn)
	{
		return () -> Minecraft.getInstance().getItemRenderer().renderQuads(matrixStack, iRenderTypeBuffer.getBuffer(
				RenderType.getTranslucent()), model.getQuads(null, null, rand, null), new ItemStack(
				Items.DIRT), combinedLightIn, combinedOverlayIn);
	}


	@SubscribeEvent
	public static void renderEvent(RenderWorldLastEvent event) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player.isSneaking()) {
			renderMachineHitBox(event, player.getHeldItem(Hand.MAIN_HAND));
		}
	}

	private static void renderMachineHitBox(RenderWorldLastEvent event, ItemStack stack) {
		if (!(stack.getItem() instanceof MachineItem))
			return;
		RayTraceResult target = Minecraft.getInstance().objectMouseOver;
		if (!(target instanceof BlockRayTraceResult))
			return;
		BlockRayTraceResult blockTarget = (BlockRayTraceResult) target;
		if (Minecraft.getInstance().world.getBlockState(blockTarget.getPos()).isAir())
			return;

		BlockPos bottomCenter = blockTarget.getPos().offset(blockTarget.getFace());
		MachineBlock block = (MachineBlock) (((MachineItem) stack.getItem()).getBlock());
		BlockRegion region = block.getBlockSize();
		MatrixStack matrixStack = event.getMatrixStack();
		GlStateManager.pushMatrix();
		GlStateManager.disableDepthTest();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		matrixStack.push();
		matrixStack.translate(bottomCenter.getX(), bottomCenter.getY(), bottomCenter.getZ());
		matrixStack.translate(region.xOff, region.yOff, region.zOff);
		Vector3d camPos = Minecraft.getInstance().getRenderManager().info.getProjectedView();
		matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);

		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(RenderType.LINES);
		Vector3f color;
		if (block.canBePlaced(Minecraft.getInstance().world, bottomCenter)) {
			color = new Vector3f(0f,1f,0f);
		} else {
			color = new Vector3f(1f, 0f, 0f);
		}
		WorldRenderer.drawShape(matrixStack, builder, region.toVoxelShape().simplify(), 0,0,0,color.getX(),color.getY(),color.getZ(),1);
		buffer.finish(RenderType.LINES);
		matrixStack.pop();
		GlStateManager.popMatrix();
	}
}
