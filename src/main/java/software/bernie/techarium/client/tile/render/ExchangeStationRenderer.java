package software.bernie.techarium.client.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.techarium.client.tile.model.BotariumModel;
import software.bernie.techarium.client.tile.model.ExchangeStationModel;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;

import javax.annotation.Nullable;

public class ExchangeStationRenderer extends GeoBlockRenderer<ExchangeStationTile>
{
	public ExchangeStationRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn, new ExchangeStationModel());
	}


	@Override
	public RenderType getRenderType(ExchangeStationTile animatable, float partialTicks, MatrixStack stack,
									@Nullable IRenderTypeBuffer renderTypeBuffer,
									@Nullable IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
	}

}
