package software.bernie.techarium.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.client.render.Color;
import software.bernie.techarium.item.MachineItem;
import software.bernie.techarium.util.BlockRegion;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Techarium.ModID, value = Dist.CLIENT)
public class RenderUtils {

	private static final float Z_FIGHTING_VALUE = 0.001f;
	public static Runnable renderModel(MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, IBakedModel model, Random rand, int combinedLightIn, int combinedOverlayIn)
	{
		return () -> Minecraft.getInstance().getItemRenderer().renderQuadList(matrixStack, iRenderTypeBuffer.getBuffer(
				RenderType.translucent()), model.getQuads(null, null, rand, null), new ItemStack(
				Items.DIRT), combinedLightIn, combinedOverlayIn);
	}

	@SubscribeEvent
	public static void renderEvent(RenderWorldLastEvent event) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player.isShiftKeyDown()) {
			renderMachineHitBox(event, player.getItemInHand(Hand.MAIN_HAND));
		}
	}

	private static void renderMachineHitBox(RenderWorldLastEvent event, ItemStack stack) {
		if (!(stack.getItem() instanceof MachineItem))
			return;
		RayTraceResult target = Minecraft.getInstance().hitResult;
		if (!(target instanceof BlockRayTraceResult))
			return;
		BlockRayTraceResult blockTarget = (BlockRayTraceResult) target;
		if (Minecraft.getInstance().level.getBlockState(blockTarget.getBlockPos()).isAir())
			return;

		BlockPos bottomCenter = blockTarget.getBlockPos().relative(blockTarget.getDirection());
		MachineBlock block = (MachineBlock) (((MachineItem) stack.getItem()).getBlock());
		BlockRegion region = block.getBlockSize();
		MatrixStack matrixStack = event.getMatrixStack();
		RenderSystem.pushMatrix();
		RenderSystem.disableDepthTest();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		matrixStack.pushPose();
		matrixStack.translate(bottomCenter.getX(), bottomCenter.getY(), bottomCenter.getZ());
		matrixStack.translate(region.xOff, region.yOff, region.zOff);
		Vector3d camPos = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
		matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);

		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		IVertexBuilder builder = buffer.getBuffer(RenderType.LINES);
		Vector3f color;
		if (block.canBePlaced(Minecraft.getInstance().level, bottomCenter)) {
			color = new Vector3f(0f,1f,0f);
		} else {
			color = new Vector3f(1f, 0f, 0f);
		}
		WorldRenderer.renderShape(matrixStack, builder, region.toVoxelShape().optimize(), 0,0,0,color.x(),color.y(),color.z(),1);
		buffer.endBatch(RenderType.LINES);
		matrixStack.popPose();
		RenderSystem.popMatrix();
	}

	private static int calculateGlowLight(int combinedLight, @Nonnull FluidStack fluid) {
		return fluid.isEmpty() ? combinedLight : calculateGlowLight(combinedLight, fluid.getFluid().getAttributes().getLuminosity(fluid));
	}

	private static int calculateGlowLight(int combinedLight, int glow) {
		return combinedLight & -65536 | Math.max(Math.min(glow, 15) << 4, combinedLight & '\uffff');
	}

	public static void renderFluid(FluidStack fluidStack, float height, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {

		Matrix4f matrix4f = matrixStack.last().pose();
		Matrix3f normal = matrixStack.last().normal();

		Fluid fluid = fluidStack.getFluid();
		FluidAttributes fluidAttributes = fluid.getAttributes();

		TextureAtlasSprite fluidTexture = getFluidStillSprite(fluidAttributes, fluidStack);

		Color color = new Color(fluidAttributes.getColor(fluidStack));

		IVertexBuilder builder = buffer.getBuffer(Atlases.translucentCullBlockSheet());
		RenderSystem.disableDepthTest();
		int light = calculateGlowLight(combinedLight, fluidStack);
		float renderHeight = Math.min(height, 1);
		for (int i = 0; i < 4; i++) {
			renderNorthFluidFace(fluidTexture, matrix4f, normal, builder, color, renderHeight, light);
			//rotate around center)
			matrixStack.translate(0.5f,0, 0.5f);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));
			matrixStack.translate(-0.5f,0, -0.5f);
		}

		if (height > 1) {
			matrixStack.translate(0,1,0);
			renderFluid(fluidStack, height - 1, matrixStack, buffer, combinedLight);
			matrixStack.translate(0, -1, 0);
		} else {
			renderTopFluidFace(fluidTexture, matrix4f, normal, builder, color, height, light);
		}
		RenderSystem.enableDepthTest();
	}

	private static void renderTopFluidFace(TextureAtlasSprite sprite, Matrix4f matrix4f, Matrix3f normalMatrix, IVertexBuilder builder, Color color, float proportion, int light) {
		float minU = sprite.getU(0);
		float maxU = sprite.getU(16);
		float minV = sprite.getV(0);
		float maxV = sprite.getV(16);

		builder.vertex(matrix4f, 0, proportion,0).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(minU, minV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();

		builder.vertex(matrix4f, 0, proportion, 1).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(minU, maxV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();

		builder.vertex(matrix4f, 1, proportion, 1).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(maxU, maxV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();

		builder.vertex(matrix4f, 1, proportion,0).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(maxU, minV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();
	}

	private static void renderNorthFluidFace(TextureAtlasSprite sprite, Matrix4f matrix4f, Matrix3f normalMatrix, IVertexBuilder builder, Color color, float proportion, int light) {

		float minU = sprite.getU(0);
		float maxU = sprite.getU(16);
		float minV = sprite.getV(0);
		float maxV = sprite.getV(16 * proportion);

		builder.vertex(matrix4f, 0, proportion, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(minU, minV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.vertex(matrix4f, 1, proportion,  Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(maxU, minV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.vertex(matrix4f, 1, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(maxU, maxV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.vertex(matrix4f, 0, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(minU, maxV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();
	}

	public static void renderWall(BlockState state, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn, int thickness) {
		EnumMap<Direction, TextureAtlasSprite> textures = getSpritesOfBlock(state);
		if (textures.size() != 6) //Not all textures found
			return;

		Matrix4f matrix4f = matrixStack.last().pose();
		Matrix3f normal = matrixStack.last().normal();
		Color color = new Color(Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0));
		IVertexBuilder builder = buffer.getBuffer(Atlases.cutoutBlockSheet());
		for (int i = 0; i < 4; i++) {
			Direction direction = Direction.from2DDataValue(i);
			renderFace(matrix4f, normal, builder,  color, textures.get(direction),packedLightIn, i%2==0 ? 16 : thickness);
			matrixStack.translate(i%2== 0 ? 1 : thickness/16f,0, 0);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(270));
		}
	}

	private static void renderFace(Matrix4f matrix4f, Matrix3f normalMatrix, IVertexBuilder builder, Color color, TextureAtlasSprite texture, int light, int width) {
		float minU = texture.getU(0);
		float maxU = texture.getU(width);
		float minV = texture.getV(0);
		float maxV = texture.getV(16);

		builder.vertex(matrix4f, 0, 1, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(minU, minV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.vertex(matrix4f, width/16f, 1,  Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(maxU, minV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.vertex(matrix4f, width/16f, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(maxU, maxV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.vertex(matrix4f, 0, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.uv(minU, maxV)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();
	}

	private static TextureAtlasSprite getFluidStillSprite(FluidAttributes attributes, FluidStack fluidStack) {
		return Minecraft.getInstance()
				.getTextureAtlas(PlayerContainer.BLOCK_ATLAS)
				.apply(attributes.getStillTexture(fluidStack));
	}

	private static EnumMap<Direction, TextureAtlasSprite> getSpritesOfBlock(BlockState state) {
		BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
		IBakedModel model = dispatcher.getBlockModel(state);
		EnumMap<Direction, TextureAtlasSprite> textures = new EnumMap<>(Direction.class);
		for (Direction d: new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN}) {
			if (model.getQuads(state, d, new Random(), EmptyModelData.INSTANCE).isEmpty()) {
				return textures;
			} else {
				textures.put(d, model.getQuads(state, d, new Random(), EmptyModelData.INSTANCE).get(0).getSprite());
			}
		}
		return textures;
	}

}
