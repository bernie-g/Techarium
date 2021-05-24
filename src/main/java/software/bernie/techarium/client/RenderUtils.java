package software.bernie.techarium.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
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
import net.minecraftforge.client.ForgeHooksClient;
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

@Mod.EventBusSubscriber(modid = Techarium.ModID)
public class RenderUtils {

	private static final float Z_FIGHTING_VALUE = 0.001f;

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

	private static int calculateGlowLight(int combinedLight, @Nonnull FluidStack fluid) {
		return fluid.isEmpty() ? combinedLight : calculateGlowLight(combinedLight, fluid.getFluid().getAttributes().getLuminosity(fluid));
	}

	private static int calculateGlowLight(int combinedLight, int glow) {
		return combinedLight & -65536 | Math.max(Math.min(glow, 15) << 4, combinedLight & '\uffff');
	}

	public static void renderFluid(FluidStack fluidStack, float height, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {

		Matrix4f matrix4f = matrixStack.getLast().getMatrix();
		Matrix3f normal = matrixStack.getLast().getNormal();

		Fluid fluid = fluidStack.getFluid();
		FluidAttributes fluidAttributes = fluid.getAttributes();

		TextureAtlasSprite fluidTexture = getFluidStillSprite(fluidAttributes, fluidStack);

		Color color = new Color(fluidAttributes.getColor(fluidStack));

		IVertexBuilder builder = buffer.getBuffer(Atlases.getTranslucentCullBlockType());
		GlStateManager.disableDepthTest();

		int light = calculateGlowLight(combinedLight, fluidStack);

		for (int i = 0; i < 4; i++) {
			renderNorthFluidFace(fluidTexture, matrix4f, normal, builder, color, height, light);
			//rotate around center)
			matrixStack.translate(0.5f,0, 0.5f);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
			matrixStack.translate(-0.5f,0, -0.5f);
		}

		renderTopFluidFace(fluidTexture, matrix4f, normal, builder, color, height, light);
		GlStateManager.enableDepthTest();
	}

	private static void renderTopFluidFace(TextureAtlasSprite sprite, Matrix4f matrix4f, Matrix3f normalMatrix, IVertexBuilder builder, Color color, float proportion, int light) {
		float minU = sprite.getInterpolatedU(0);
		float maxU = sprite.getInterpolatedU(16);
		float minV = sprite.getInterpolatedV(0);
		float maxV = sprite.getInterpolatedV(16);

		builder.pos(matrix4f, 0, proportion,0).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();

		builder.pos(matrix4f, 0, proportion, 1).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();

		builder.pos(matrix4f, 1, proportion, 1).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();

		builder.pos(matrix4f, 1, proportion,0).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();
	}

	private static void renderNorthFluidFace(TextureAtlasSprite sprite, Matrix4f matrix4f, Matrix3f normalMatrix, IVertexBuilder builder, Color color, float proportion, int light) {

		float minU = sprite.getInterpolatedU(0);
		float maxU = sprite.getInterpolatedU(16);
		float minV = sprite.getInterpolatedV(0);
		float maxV = sprite.getInterpolatedV(16 * proportion);

		builder.pos(matrix4f, 0, proportion, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.pos(matrix4f, 1, proportion,  Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.pos(matrix4f, 1, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.pos(matrix4f, 0, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();
	}

	public static void renderWall(BlockState state, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn, int thickness) {
		EnumMap<Direction, TextureAtlasSprite> textures = getSpritesOfBlock(state);
		if (textures.size() != 6) //Not all textures found
			return;

		Matrix4f matrix4f = matrixStack.getLast().getMatrix();
		Matrix3f normal = matrixStack.getLast().getNormal();
		Color color = new Color(Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0));
		IVertexBuilder builder = buffer.getBuffer(Atlases.getCutoutBlockType());
		for (int i = 0; i < 4; i++) {
			Direction direction = Direction.byHorizontalIndex(i);
			renderFace(matrix4f, normal, builder,  color, textures.get(direction),packedLightIn, i%2==0 ? 16 : thickness);
			matrixStack.translate(i%2== 0 ? 1 : thickness/16f,0, 0);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(270));
		}
	}

	private static void renderFace(Matrix4f matrix4f, Matrix3f normalMatrix, IVertexBuilder builder, Color color, TextureAtlasSprite texture, int light, int width) {
		float minU = texture.getInterpolatedU(0);
		float maxU = texture.getInterpolatedU(width);
		float minV = texture.getInterpolatedV(0);
		float maxV = texture.getInterpolatedV(16);

		builder.pos(matrix4f, 0, 1, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.pos(matrix4f, width/16f, 1,  Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.pos(matrix4f, width/16f, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.pos(matrix4f, 0, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(light)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();
	}

	private static TextureAtlasSprite getFluidStillSprite(FluidAttributes attributes, FluidStack fluidStack) {
		return Minecraft.getInstance()
				.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
				.apply(attributes.getStillTexture(fluidStack));
	}

	private static EnumMap<Direction, TextureAtlasSprite> getSpritesOfBlock(BlockState state) {
		BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
		IBakedModel model = dispatcher.getModelForState(state);
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
