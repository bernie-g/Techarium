package software.bernie.techarium.client.tile.render;

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
}
