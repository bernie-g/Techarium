package software.bernie.techarium.client.tile.render.arboretum;

import com.mojang.blaze3d.matrix.MatrixStack;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.EmptyModelData;
import software.bernie.techarium.registry.ParticlesRegistry;
import software.bernie.techarium.tile.arboretum.ArboretumTile;

public class ArboretumTreeRender {

	@Getter
	private static ArboretumTreeRender instance = new ArboretumTreeRender();
	
	@Getter
	@Setter
	private Block log = Blocks.OAK_LOG;
	
	@Getter
	@Setter
	private Block leaves = Blocks.OAK_LEAVES;

	private float scale = 0;
	
	public void renderTree(MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, int packedOverlayIn, float partialTicks, float craftPercent) {
		scale = craftPercent / 10f;
		stack.translate(-scale/2f, 0.8, -scale/2f);
		stack.scale(scale, scale, scale);
		
		renderLogGrow(stack, renderTypeBuffer, packedLightIn, packedOverlayIn, partialTicks, craftPercent);
		renderLeavesGrow(stack, renderTypeBuffer, packedLightIn, packedOverlayIn, partialTicks, craftPercent);
	}
	
	private void renderLogGrow(MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, int packedOverlayIn, float partialTicks, float craftPercent) {
		for (float i = 0; i < (float) 6f * craftPercent; i++) {
			stack.pushPose();
			stack.translate(0, i, 0);
			Minecraft.getInstance().getBlockRenderer().renderBlock(log.defaultBlockState(), stack, renderTypeBuffer, packedLightIn, packedOverlayIn, EmptyModelData.INSTANCE);
			stack.popPose();
		}
	}

	private void renderLeavesGrow(MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, int packedOverlayIn, float partialTicks, float craftPercent) {			
		if (craftPercent < 0.25)
			return;
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = 0; k < 2; k++) {
					if (k == 1) {
						if ((i == -1 && j == -1) || (i == 1 && j == -1) || (i == 1 && j == 1) || (i == -1 && j == 1))
							continue;
					}	
					stack.pushPose();
					stack.translate(i, k + (float) 6f * craftPercent, j);
					Minecraft.getInstance().getBlockRenderer().renderBlock(leaves.defaultBlockState(), stack, renderTypeBuffer, packedLightIn, packedOverlayIn, EmptyModelData.INSTANCE);
					stack.popPose();
				}
			}
		}
		
		if (craftPercent < 0.5)
			return;
		
		int radius = (int) (2.5f * Math.min(1, ((craftPercent * 2f) - 1f) + 0.1f));
		
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = -2; k < 0; k++) {
					if (i == 0 && j == 0) continue;
					stack.pushPose();
					stack.translate(i, k + (float) 6f * craftPercent, j);
					Minecraft.getInstance().getBlockRenderer().renderBlock(leaves.defaultBlockState(), stack, renderTypeBuffer, packedLightIn, packedOverlayIn, EmptyModelData.INSTANCE);
					stack.popPose();
				}
			}
		}
	}

	
}
