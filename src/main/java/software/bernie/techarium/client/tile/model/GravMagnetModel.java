package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.gravmagnet.GravMagnetTile;

public class GravMagnetModel extends AnimatedGeoModel<GravMagnetTile>
{
	@Override
	public ResourceLocation getAnimationFileLocation(GravMagnetTile tile) {
		return new ResourceLocation(Techarium.MOD_ID, "animations/gravmagnet.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(GravMagnetTile tile) {
		return new ResourceLocation(Techarium.MOD_ID, "geo/gravmagnet/gravmagnet.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(GravMagnetTile tile) {
		String mode = tile.isPull() ? "pull" : "push";		
		return new ResourceLocation(Techarium.MOD_ID, "textures/block/animated/gravmagnet_" + mode + ".png");
	}
}
