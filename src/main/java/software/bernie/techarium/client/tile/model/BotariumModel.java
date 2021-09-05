package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.botarium.BotariumTile;

public class BotariumModel extends AnimatedGeoModel<BotariumTile>
{
	@Override
	public ResourceLocation getAnimationFileLocation(BotariumTile tile) {
		return new ResourceLocation(Techarium.MOD_ID, "animations/botarium.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(BotariumTile tile) {
		return new ResourceLocation(Techarium.MOD_ID, "geo/botarium/botarium.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(BotariumTile tile) {
		return new ResourceLocation(Techarium.MOD_ID, "textures/block/animated/botarium.png");
	}
}
