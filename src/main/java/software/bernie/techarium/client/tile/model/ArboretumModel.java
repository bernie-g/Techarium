package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.arboretum.ArboretumTile;

public class ArboretumModel extends AnimatedGeoModel<ArboretumTile>
{
	@Override
	public ResourceLocation getAnimationFileLocation(ArboretumTile tile) {
		return Techarium.rl("animations/arboretum.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(ArboretumTile tile) {
		return Techarium.rl("geo/arboretum/arboretum.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(ArboretumTile tile) {
		return Techarium.rl("textures/block/animated/arboretum.png");
	}
}
