package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.botarium.BotariumTile;

public class ArboretumModel extends AnimatedGeoModel<ArboretumTile>
{
	@Override
	public ResourceLocation getAnimationFileLocation(ArboretumTile tile) {
		return new ResourceLocation(Techarium.ModID, "animations/arboretum.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(ArboretumTile tile) {
		return new ResourceLocation(Techarium.ModID, "geo/arboretum/arboretum.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(ArboretumTile tile) {
		return new ResourceLocation(Techarium.ModID, "textures/block/animated/arboretum.png");
	}
}
