package software.bernie.techarium.client.tile.render.arboretum;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.techarium.client.tile.model.ArboretumModel;
import software.bernie.techarium.helper.EventHelper;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.recipe.recipe.ArboretumRecipe;
import software.bernie.techarium.tile.arboretum.ArboretumTile;

public class ArboretumRenderer extends GeoBlockRenderer<ArboretumTile> {
	
	private float progress = 0;
	
	private Block log = Blocks.OAK_LOG;
	private Block leaf = Blocks.OAK_LEAVES;
	private Map<Block, Block> cache = new HashMap<>();
 	
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
		renderTree(tile, partialTicks, stack, renderTypeBuffer, packedLightIn, packedOverlayIn);
		
	}

	private void renderTree(ArboretumTile tile, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, int packedLightIn, int packedOverlayIn) {
		MachineController<ArboretumRecipe> controler = tile.getController();
		
		controler.getMultiProgressBar().getProgressBarAddons().forEach(bar -> {
			progress = (float) bar.getProgress() / bar.getMaxProgress();
		});
				
		log = tile.getLogOutput();
		
		if (log == null)
			return;
		
		leaf = Blocks.OAK_LEAVES;

		if (cache.containsKey(log)) {
			leaf = cache.get(log);
		} else {
			Ingredient sapling = controler.getCurrentRecipe().getCropType();
			Block refBlock = sapling.isEmpty() ? log : Block.byItem(sapling.getItems()[0].getItem());
			String toRemove = sapling.isEmpty() ? "log" : "sapling";
			
			String logName = refBlock.getDescriptionId();
			String logMod = refBlock.getRegistryName().getNamespace();
			String key = removeUnusedChar(logName, logMod).replace(toRemove, "");
			
			ForgeRegistries.BLOCKS.forEach(block -> {
				String blockName = block.getDescriptionId();
				String blockMod = block.getRegistryName().getNamespace();
				String blockKey = removeUnusedChar(blockName, blockMod).replace("leaves", "");
				
				if (key.equals(blockKey)) {
					cache.put(log, block);
				}	
			});
		}
				
		ArboretumTreeRender treeRender = ArboretumTreeRender.getInstance();
		treeRender.setLog(log);
		treeRender.setLeaves(leaf);
		
		stack.pushPose();
		treeRender.renderTree(stack, renderTypeBuffer, packedLightIn, packedOverlayIn, partialTicks, progress);
		stack.popPose();
	}

	private String removeUnusedChar(String str, String mod) {
		return str.replace("block", "")
				.replace("_", "")
				.replace(".", "")
				.replace(mod, "");
	}
	
	@Override
	public void render(TileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
					   int combinedLightIn, int combinedOverlayIn) {
		this.render((ArboretumTile) tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
	}

	public void renderTile(ArboretumTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn, int combinedOverlayIn) {
		matrixStack.pushPose();
		matrixStack.translate(-0.25,0.3125,-0.25);
		matrixStack.scale(0.5f, 0.5f, 0.5f);
		Minecraft.getInstance().getBlockRenderer().renderBlock(getRenderSoilBlock(tile), matrixStack, buffer, packedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
		matrixStack.popPose();
	}

	private static boolean testALlDirt(Ingredient ingredient) {
		for (Block dirt : Tags.Blocks.DIRT.getValues()) {
			if (!ingredient.test(dirt.asItem().getDefaultInstance())) return false;
		}
		return true;
	}

	private BlockState getRenderSoilBlock(ArboretumTile tile) {
		if (EventHelper.isChristmas()) return Blocks.SNOW_BLOCK.defaultBlockState();
				
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

	@Override
	public RenderType getRenderType(ArboretumTile animatable, float partialTicks, MatrixStack stack,
									@Nullable IRenderTypeBuffer renderTypeBuffer,
									@Nullable IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
	}
}
