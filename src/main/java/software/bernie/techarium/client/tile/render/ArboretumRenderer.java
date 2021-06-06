package software.bernie.techarium.client.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StemBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.common.Tags;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.techarium.client.RenderUtils;
import software.bernie.techarium.client.tile.model.ArboretumModel;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;
import software.bernie.techarium.tile.arboretum.ArboretumTile;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

public class ArboretumRenderer extends GeoBlockRenderer<ArboretumTile> {


	public ArboretumRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn, new ArboretumModel());
	}

	@Override
	public void renderEarly(ArboretumTile tile, MatrixStack stack, float ticks, IRenderTypeBuffer renderTypeBuffer,  IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		stack.pushPose();
		stack.translate(0, 0.59, -0.48);
		stack.mulPose(new Quaternion(25, 180, 0, true));
		stack.scale(0.21f, 0.21f, 0.21f);
		Minecraft.getInstance().getItemRenderer().renderStatic(tile.getCropInventory().getStackInSlot(0), ItemCameraTransforms.TransformType.NONE, packedLightIn, packedOverlayIn, stack, renderTypeBuffer);
		stack.popPose();
		renderTile(tile, partialTicks, stack, renderTypeBuffer, packedLightIn, packedOverlayIn);
	}

	@Override
	public void render(TileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
					   int combinedLightIn, int combinedOverlayIn) {
		this.render((ArboretumTile) tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
	}

	public void renderTile(ArboretumTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn, int combinedOverlayIn) {
		matrixStack.pushPose();
		matrixStack.translate(-0.5f,0,-0.5f);
		matrixStack.translate(4 / 16f, 5 / 16f, 4 / 16f);
		renderCrop(tile, matrixStack, partialTicks, buffer, packedLightIn, combinedOverlayIn);
		matrixStack.popPose();
	}

	private void renderCrop(ArboretumTile tile, MatrixStack matrixStack, float partialTicks, IRenderTypeBuffer buffer, int packedLightIn, int combinedOverlayIn) {
		matrixStack.pushPose();
		matrixStack.scale(0.5f, 0.5f, 0.5f);
		Minecraft.getInstance().getBlockRenderer().renderBlock(getRenderSoilBlock(tile), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

		int age = getAge(tile, IntegerProperty.create("math",0,8));
		matrixStack.scale(age*0.04f,age*0.04f,age*0.04f);
		Minecraft.getInstance().getBlockRenderer().renderBlock(getRenderTreeBlock(tile), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

		matrixStack.popPose();
	}

	private static boolean testALlDirt(Ingredient ingredient) {
		for (Block dirt : Tags.Blocks.DIRT.getValues()) {
			if (!ingredient.test(dirt.asItem().getDefaultInstance())) return false;
		}
		return true;
	}

	private BlockState getRenderSoilBlock(ArboretumTile tile) {
		ItemStack soil = tile.getSoilInventory().getStackInSlot(0);
		if (soil.isEmpty() || !(soil.getItem() instanceof BlockItem))
			return Blocks.AIR.defaultBlockState();
		BlockState state = ((BlockItem) soil.getItem()).getBlock().defaultBlockState();
		if (state.is(Tags.Blocks.DIRT) && !(tile.getController().getCurrentRecipe() != null && !testALlDirt(tile.getController().getCurrentRecipe().getSoilIn()))) {
			state = Blocks.FARMLAND.defaultBlockState();
		}
		if (state.hasProperty(BlockStateProperties.MOISTURE))
			state = state.setValue(BlockStateProperties.MOISTURE, 7);
		return state;
	}

	private BlockState getRenderTreeBlock(ArboretumTile tile) {
		ItemStack crop = tile.getCropInventory().getStackInSlot(0);
		if (crop.isEmpty() || !(crop.getItem() instanceof BlockItem))
			return Blocks.AIR.defaultBlockState();
		BlockState state = ((BlockItem) crop.getItem()).getBlock().defaultBlockState();
		return state;
	}

	private static int getAge(ArboretumTile tile, IntegerProperty property) {
		int max = property.getPossibleValues().stream().max(Comparator.comparing(integer -> integer)).orElse(1);
		int min = property.getPossibleValues().stream().min(Comparator.comparing(integer -> integer)).orElse(0);
		int allStates = property.getPossibleValues().size();
		return (int) Math.min(Math.floor(allStates*getMachineProgress(tile)) + min, max);
	}

	private static float getMachineProgress(ArboretumTile tile) {
		ProgressBarAddon machineProgress = getMachineProgressBarAddon(tile);
		float realProgress = machineProgress.getProgress()/ (float)machineProgress.getMaxProgress();
		return Math.min(1f, realProgress);
	}

	private static ProgressBarAddon getMachineProgressBarAddon(ArboretumTile tile) {
		for (ProgressBarAddon progressBarAddon : tile.getController().getMultiProgressBar().getProgressBarAddons()) {
			if (progressBarAddon.getName().equals("techarium.gui.mainprogress")) {
				return progressBarAddon;
			}
		}
		throw new NullPointerException("No progressbar found");
	}

	@Override
	public RenderType getRenderType(ArboretumTile animatable, float partialTicks, MatrixStack stack,
									@Nullable IRenderTypeBuffer renderTypeBuffer,
									@Nullable IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
	}
}
