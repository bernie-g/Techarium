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
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.techarium.client.tile.model.ArboretumModel;
import software.bernie.techarium.client.tile.model.AssemblerModel;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.assembler.AssemblerTile;

import javax.annotation.Nullable;

public class AssemblerRenderer extends GeoBlockRenderer<AssemblerTile> {
	public AssemblerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn, new AssemblerModel());
	}
	
	@Override
	public RenderType getRenderType(AssemblerTile animatable, float partialTicks, MatrixStack stack,
									@Nullable IRenderTypeBuffer renderTypeBuffer,
									@Nullable IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
	}
}
