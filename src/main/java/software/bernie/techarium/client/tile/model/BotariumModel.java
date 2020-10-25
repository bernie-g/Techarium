package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.BotariumTile;

public class BotariumModel extends AnimatedGeoModel<BotariumTile>
{
	@Override
	public ResourceLocation getAnimationFileLocation(BotariumTile tile)
	{
		return new ResourceLocation(Techarium.ModID, "animations/botarium_anim.json");
	}

	@Override
	public ResourceLocation getModelLocation(BotariumTile tile)
	{
		ResourceLocation location;
		switch(tile.getActiveController().getTier())
		{
			case 1:
				location = new ResourceLocation(Techarium.ModID, "geo/botarium/botarium_tier_1.json");
				break;
			case 2:
				location = new ResourceLocation(Techarium.ModID, "geo/botarium/botarium_tier_1.json");
				break;
			case 3:
				location = new ResourceLocation(Techarium.ModID, "geo/botarium/botarium_tier_1.json");
				break;
			case 4:
				location = new ResourceLocation(Techarium.ModID, "geo/botarium/botarium_tier_1.json");
				break;
			default:
				location = new ResourceLocation(Techarium.ModID, "geo/botarium/botarium_tier_1.json");
				break;
		}
		return location;
	}

	@Override
	public ResourceLocation getTextureLocation(BotariumTile tile)
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
