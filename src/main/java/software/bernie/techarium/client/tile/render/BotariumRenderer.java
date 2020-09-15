package software.bernie.techarium.client.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.geo.render.GeoBlockRenderer;
import software.bernie.geckolib.geo.render.built.GeoModel;
import software.bernie.geckolib.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.client.tile.model.BotariumModel;
import software.bernie.techarium.tile.BotariumTile;

import java.awt.*;

public class BotariumRenderer extends GeoBlockRenderer<BotariumTile>
{
	public BotariumRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new BotariumModel());
	}

	@Override
	public ResourceLocation getTexture(BotariumTile tile)
	{
		ResourceLocation location;
		switch (tile.getActiveController().getTier())
		{
			case 1:
				location = new ResourceLocation(Techarium.ModID, "textures/block/animated/botarium_tier_1.png");
				break;
			case 2:
				location = new ResourceLocation(Techarium.ModID, "textures/block/animated/botarium_tier_1.png");
				break;
			case 3:
				location = new ResourceLocation(Techarium.ModID, "textures/block/animated/botarium_tier_1.png");
				break;
			case 4:
				location = new ResourceLocation(Techarium.ModID, "textures/block/animated/botarium_tier_1.png");
				break;
			default:
				location = new ResourceLocation(Techarium.ModID, "textures/block/animated/botarium_tier_1.png");
				break;
		}
		return location;
	}

	@Override
	public void renderEarly(BotariumTile tile, MatrixStack matrixStackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		matrixStackIn.push();
		matrixStackIn.translate(0, 0.61, -0.5);
		matrixStackIn.rotate(new Quaternion(25, 0, 0, true));
		matrixStackIn.scale(0.21f, 0.21f, 0.21f);
		Minecraft.getInstance().getItemRenderer().renderItem(tile.getCropInventory().getStackInSlot(0), ItemCameraTransforms.TransformType.NONE, packedLightIn, packedOverlayIn, matrixStackIn, renderTypeBuffer);
		matrixStackIn.pop();
	}

}
