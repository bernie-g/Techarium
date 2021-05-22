package software.bernie.techarium.client.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.techarium.client.render.Color;
import software.bernie.techarium.client.tile.model.BotariumModel;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;
import software.bernie.techarium.tile.botarium.BotariumTile;

import javax.annotation.Nullable;
import java.util.Comparator;

public class BotariumRenderer extends GeoBlockRenderer<BotariumTile> {

	private static final float Z_FIGHTING_VALUE = 0.001f;

	public BotariumRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn, new BotariumModel());
	}

	@Override
	public void renderEarly(BotariumTile tile, MatrixStack stack, float ticks, IRenderTypeBuffer renderTypeBuffer,  IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		stack.push();
		stack.translate(0, 0.61, -0.47);
		stack.rotate(new Quaternion(25, 0, 0, true));
		stack.scale(0.21f, 0.21f, 0.21f);
		Minecraft.getInstance().getItemRenderer().renderItem(tile.getCropInventory().getStackInSlot(0), ItemCameraTransforms.TransformType.NONE, packedLightIn, packedOverlayIn, stack, renderTypeBuffer);
		stack.pop();
	}

	@Override
	public void render(TileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
					   int combinedLightIn, int combinedOverlayIn) {
		this.render((BotariumTile) tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
		renderTile((BotariumTile) tile, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}

	public void renderTile(BotariumTile tile, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn, int combinedOverlayIn) {
		matrixStack.push();
		matrixStack.translate(4 / 16f, 5 / 16f, 4 / 16f);
		matrixStack.scale(0.5f, 0.5f, 0.5f);
		Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(getRenderSoilBlock(tile), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
		matrixStack.translate(0,1, 0);
		Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(getRenderCropBlock(tile), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
		if (!tile.getFluidInventory().isEmpty()) {
			renderFluidInTank(tile.getFluidInventory().getFluid(), matrixStack, buffer, 2/16f);
		}
		matrixStack.pop();

	}

	private void renderFluidInTank(FluidStack fluidStack, MatrixStack matrix, IRenderTypeBuffer buffer, float proportion) {
		matrix.push();
		matrix.translate(-1/16d, -4/16d, -1/16d);
		matrix.scale(18/16f,18/16f,18/16f); //2 voxel more then parent per block
		Matrix4f matrix4f = matrix.getLast().getMatrix();
		Matrix3f normal = matrix.getLast().getNormal();

		Fluid fluid = fluidStack.getFluid();
		FluidAttributes fluidAttributes = fluid.getAttributes();
		TextureAtlasSprite fluidTexture = getFluidStillSprite(fluidAttributes, fluidStack);

		Color color = new Color(fluidAttributes.getColor(fluidStack));

		IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());

		for (int i = 0; i < 4; i++) {
			renderNorthFluidFace(fluidTexture, matrix4f, normal, builder, color, proportion);
			//rotate around center)
			matrix.translate(0.5f,0, 0.5f);
			matrix.rotate(Vector3f.YP.rotationDegrees(90));
			matrix.translate(-0.5f,0, -0.5f);
		}

		renderTopFluidFace(fluidTexture, matrix4f, normal, builder, color, proportion);
		matrix.pop();
	}

	private void renderTopFluidFace(TextureAtlasSprite sprite, Matrix4f matrix4f, Matrix3f normalMatrix, IVertexBuilder builder, Color color, float proportion) {
		float minU = sprite.getInterpolatedU(0);
		float maxU = sprite.getInterpolatedU(16);
		float minV = sprite.getInterpolatedV(0);
		float maxV = sprite.getInterpolatedV(16);

		builder.pos(matrix4f, 0, proportion,0).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();

		builder.pos(matrix4f, 0, proportion, 1).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();

		builder.pos(matrix4f, 1, proportion, 1).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();

		builder.pos(matrix4f, 1, proportion,0).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880)
				.normal(normalMatrix, 0, 1, 0)
				.endVertex();
	}

	private void renderNorthFluidFace(TextureAtlasSprite sprite, Matrix4f matrix4f, Matrix3f normalMatrix, IVertexBuilder builder, Color color, float proportion) {

		float minU = sprite.getInterpolatedU(0);
		float maxU = sprite.getInterpolatedU(16);
		float minV = sprite.getInterpolatedV(0);
		float maxV = sprite.getInterpolatedV(16 * proportion);

		builder.pos(matrix4f, 0, proportion, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.pos(matrix4f, 1, proportion,  Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, minV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.pos(matrix4f, 1, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(maxU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();

		builder.pos(matrix4f, 0, 0, Z_FIGHTING_VALUE).color(color.getR(), color.getG(), color.getB(), color.getA())
				.tex(minU, maxV)
				.overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880)
				.normal(normalMatrix, 0, 0, 1)
				.endVertex();
	}

	private TextureAtlasSprite getFluidStillSprite(FluidAttributes attributes, FluidStack fluidStack) {
		return Minecraft.getInstance()
				.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
				.apply(attributes.getStillTexture(fluidStack));
	}

	private static boolean testALlDirt(Ingredient ingredient) {
		for (Block dirt : Tags.Blocks.DIRT.getAllElements()) {
			if (!ingredient.test(dirt.asItem().getDefaultInstance())) return false;
		}
		return true;
	}

	private BlockState getRenderSoilBlock(BotariumTile tile) {
		ItemStack soil = tile.getSoilInventory().getStackInSlot(0);
		if (soil.isEmpty() || !(soil.getItem() instanceof BlockItem))
			return Blocks.AIR.getDefaultState();
		BlockState state = ((BlockItem) soil.getItem()).getBlock().getDefaultState();
		if (state.isIn(Tags.Blocks.DIRT) && !(tile.getController().getCurrentRecipe() != null && !testALlDirt(tile.getController().getCurrentRecipe().getSoilIn()))) {
			state = Blocks.FARMLAND.getDefaultState();
		}
		if (state.hasProperty(BlockStateProperties.MOISTURE_0_7))
			state = state.with(BlockStateProperties.MOISTURE_0_7, 7);
		return state;
	}

	private BlockState getRenderCropBlock(BotariumTile tile) {
		ItemStack crop = tile.getCropInventory().getStackInSlot(0);
		if (crop.isEmpty() || !(crop.getItem() instanceof BlockItem))
			return Blocks.AIR.getDefaultState();
		BlockState state = ((BlockItem) crop.getItem()).getBlock().getDefaultState();
		state = withAgeProperty(tile, state);
		return state;
	}

	private static BlockState withAgeProperty(BotariumTile tile, BlockState state) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals("age") && property instanceof IntegerProperty) {
				return state.with((IntegerProperty)property, getAge(tile, (IntegerProperty) property));
			}
		}
		return state;
	}

	private static int getAge(BotariumTile tile, IntegerProperty property) {
		int max = property.getAllowedValues().stream().max(Comparator.comparing(integer -> integer)).orElse(1);
		int min = property.getAllowedValues().stream().min(Comparator.comparing(integer -> integer)).orElse(0);
		int allStates = property.getAllowedValues().size();
		return (int) Math.min(Math.floor(allStates*getMachineProgress(tile)) + min, max);
	}

	private static float getMachineProgress(BotariumTile tile) {
		for (ProgressBarAddon progressBarAddon : tile.getController().getMultiProgressBar().getProgressBarAddons()) {
			if (progressBarAddon.getName().equals("techarium.gui.mainprogress")) {
				return progressBarAddon.getProgress() / (float)progressBarAddon.getMaxProgress();
			}
		}
		return 0f;
	}

	@Override
	public RenderType getRenderType(BotariumTile animatable, float partialTicks, MatrixStack stack,
									@Nullable IRenderTypeBuffer renderTypeBuffer,
									@Nullable IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.getEntityCutoutNoCull(this.getTextureLocation(animatable));
	}
}
