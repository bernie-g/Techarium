package software.bernie.techarium.client.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.geo.render.GeoBlockRenderer;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.client.tile.model.BotariumModel;
import software.bernie.techarium.tile.BotariumTile;

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
	public void renderCustom(BotariumTile tile, MatrixStack stackIn, float ticks, MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		stackIn.push();
		stackIn.translate(0, 0.61, -0.5);
		stackIn.rotate(new Quaternion(25, 0, 0, true));
		stackIn.scale(0.21f, 0.21f, 0.21f);
		Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getInstance().getItemRenderer().renderItem(tile.getCropInventory().getStackInSlot(0), ItemCameraTransforms.TransformType.NONE, packedLightIn, packedOverlayIn, matrixStackIn, renderTypeBuffer);
		Minecraft.getInstance().getTextureManager().bindTexture(getTexture(tile));
		stackIn.pop();
	}
}
