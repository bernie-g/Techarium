package software.bernie.techarium.client.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.*;
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
import software.bernie.techarium.client.tile.model.BotariumModel;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;
import software.bernie.techarium.tile.botarium.BotariumTile;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

public class BotariumRenderer extends GeoBlockRenderer<BotariumTile> {


	public BotariumRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn, new BotariumModel());
	}

	@Override
	public void renderEarly(BotariumTile tile, MatrixStack stack, float ticks, IRenderTypeBuffer renderTypeBuffer,  IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
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
		this.render((BotariumTile) tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
	}

	public void renderTile(BotariumTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn, int combinedOverlayIn) {
		matrixStack.pushPose();
		matrixStack.translate(-0.5f,0,-0.5f);
		matrixStack.translate(4 / 16f, 5 / 16f, 4 / 16f);
		renderCrop(tile, matrixStack, partialTicks, buffer, packedLightIn, combinedOverlayIn);
		matrixStack.scale(0.5f, 0.5f, 0.5f);
		matrixStack.translate(0,1,0);
		if (!tile.getFluidInventory().isEmpty()) {
			matrixStack.pushPose();
			matrixStack.translate(-1/16d, -4/16d, -1/16d);
			matrixStack.scale(18/16f,18/16f,18/16f); //2 voxel more then parent per block
			RenderUtils.renderFluid(tile.getFluidInventory().getFluid(), getGrowthType(tile) == GrowthType.AQUA ? 2 : 2/16f, matrixStack, buffer, packedLightIn);
			matrixStack.popPose();
		}

		matrixStack.popPose();
	}

	private void renderCrop(BotariumTile tile, MatrixStack matrixStack, float partialTicks, IRenderTypeBuffer buffer, int packedLightIn, int combinedOverlayIn) {
		matrixStack.pushPose();
		matrixStack.scale(0.5f, 0.5f, 0.5f);
		if (getGrowthType(tile) != GrowthType.WALL) {
			Minecraft.getInstance().getBlockRenderer().renderBlock(getRenderSoilBlock(tile), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
		}
		switch (getGrowthType(tile)) {
			case DEFAULT:
			case AQUA:
				matrixStack.translate(0,1, 0);
				Minecraft.getInstance().getBlockRenderer().renderBlock(getRenderCropBlock(tile), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
				break;
			case FROM_BOTTOM:
				int age = getAge(tile, IntegerProperty.create("math",1,8));
				matrixStack.translate(0, age*2f/16f - 0.0015f, 0); //-0.0015f for z-fighting
				Minecraft.getInstance().getBlockRenderer().renderBlock(getRenderCropBlock(tile), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
				break;
			case WITH_STEM:
				matrixStack.translate(0,1,4/16f);
				Minecraft.getInstance().getBlockRenderer().renderBlock(getRenderStemCropBlock(tile), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
				if (getMachineProgress(tile) > 0.5f) {
					age = getAge(tile, IntegerProperty.create("math",0,8));

					matrixStack.translate(8f/16f,9f/16f,0f/16f);
					matrixStack.scale(age*0.04f,age*0.04f,age*0.04f);
					matrixStack.translate(-0.5f,-0.5f, -0.5f);
					ItemStack crop = tile.getCropInventory().getStackInSlot(0);
					StemBlock stem = ((StemBlock) ((BlockItem) crop.getItem()).getBlock());
					Minecraft.getInstance().getBlockRenderer().renderBlock(stem.getFruit().defaultBlockState(), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
				}
				break;
			case WALL:
				BlockState state = getRenderSoilBlock(tile);
				if (state.isAir())
					break;
				matrixStack.translate(0,12/16f, 1 - 5/16f);
				RenderUtils.renderWall(state, matrixStack, buffer, packedLightIn, 6);
				matrixStack.translate(0,1, 0);
				RenderUtils.renderWall(state, matrixStack, buffer, packedLightIn, 6);
				matrixStack.translate(0,-0.5, -1);
				state = getRenderCropBlock(tile);
				state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
				Minecraft.getInstance().getBlockRenderer().renderBlock(state, matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
				break;
		}
		matrixStack.popPose();
	}

	private GrowthType getGrowthType(BotariumTile tile) {
		ItemStack stack = tile.getCropInventory().getStackInSlot(0);
		if (stack.isEmpty()) return GrowthType.DEFAULT;
		if (stack.getItem() instanceof BlockItem) {
			Block block = ((BlockItem) stack.getItem()).getBlock();
			if (block instanceof StemBlock) {
				return GrowthType.WITH_STEM;
			} else if (block == Blocks.COCOA) {
				return GrowthType.WALL;
			} else if (block == Blocks.SUGAR_CANE || block == Blocks.CACTUS) {
				return GrowthType.FROM_BOTTOM;
			} else if (block == Blocks.KELP) {
				return GrowthType.AQUA;
			}
		}
		return GrowthType.DEFAULT;
	}

	private static boolean testALlDirt(Ingredient ingredient) {
		for (Block dirt : Tags.Blocks.DIRT.getValues()) {
			if (!ingredient.test(dirt.asItem().getDefaultInstance())) return false;
		}
		return true;
	}

	private BlockState getRenderSoilBlock(BotariumTile tile) {
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

	private BlockState getRenderStemCropBlock(BotariumTile tile) {
		ItemStack crop = tile.getCropInventory().getStackInSlot(0);
		if (crop.isEmpty() || !(crop.getItem() instanceof BlockItem))
			return Blocks.AIR.defaultBlockState();

		if (getMachineProgress(tile) > 0.5f) {
			StemBlock stem = ((StemBlock)((BlockItem) crop.getItem()).getBlock());
			return stem.getFruit().getAttachedStem().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
		} else {
			BlockState state = ((BlockItem) crop.getItem()).getBlock().defaultBlockState();
			return withStemAgeProperty(tile, state);
		}
	}
	private static BlockState withStemAgeProperty(BotariumTile tile, BlockState state) {
		Optional<IntegerProperty> ageProperty = getAgeProperty(state);
		return ageProperty.map(integerProperty -> state.setValue(integerProperty, getStemAge(tile, integerProperty))).orElse(state);
	}
	private static int getStemAge(BotariumTile tile, IntegerProperty property) {
		int max = property.getPossibleValues().stream().max(Comparator.comparing(integer -> integer)).orElse(1);
		int min = property.getPossibleValues().stream().min(Comparator.comparing(integer -> integer)).orElse(0);
		int allStates = property.getPossibleValues().size();
		return (int) Math.min(Math.floor(allStates*(getMachineProgress(tile)*2f)) + min, max);
	}
	private BlockState getRenderCropBlock(BotariumTile tile) {
		ItemStack crop = tile.getCropInventory().getStackInSlot(0);
		if (crop.isEmpty() || !(crop.getItem() instanceof BlockItem))
			return Blocks.AIR.defaultBlockState();
		BlockState state = ((BlockItem) crop.getItem()).getBlock().defaultBlockState();
		state = withAgeProperty(tile, state);
		return state;
	}

	private static Optional<IntegerProperty> getAgeProperty(BlockState state) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals("age") && property instanceof IntegerProperty) {
				return Optional.of((IntegerProperty) property);
			}
		}
		return Optional.empty();
	}

	private static BlockState withAgeProperty(BotariumTile tile, BlockState state) {
		Optional<IntegerProperty> ageProperty = getAgeProperty(state);
		return ageProperty.map(integerProperty -> state.setValue(integerProperty, getAge(tile, integerProperty))).orElse(state);
	}

	private static int getAge(BotariumTile tile, IntegerProperty property) {
		int max = property.getPossibleValues().stream().max(Comparator.comparing(integer -> integer)).orElse(1);
		int min = property.getPossibleValues().stream().min(Comparator.comparing(integer -> integer)).orElse(0);
		int allStates = property.getPossibleValues().size();
		return (int) Math.min(Math.floor(allStates*getMachineProgress(tile)) + min, max);
	}

	private static float getMachineProgress(BotariumTile tile) {
		ProgressBarAddon machineProgress = getMachineProgressBarAddon(tile);
		float realProgress = machineProgress.getProgress()/ (float)machineProgress.getMaxProgress();
		return Math.min(1f, realProgress);
	}

	private static ProgressBarAddon getMachineProgressBarAddon(BotariumTile tile) {
		for (ProgressBarAddon progressBarAddon : tile.getController().getMultiProgressBar().getProgressBarAddons()) {
			if (progressBarAddon.getName().equals("techarium.gui.mainprogress")) {
				return progressBarAddon;
			}
		}
		throw new NullPointerException("No progressbar found");
	}

	@Override
	public RenderType getRenderType(BotariumTile animatable, float partialTicks, MatrixStack stack,
									@Nullable IRenderTypeBuffer renderTypeBuffer,
									@Nullable IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
	}
}
