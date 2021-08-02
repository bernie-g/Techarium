package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.gravmagnet.GravMagnetTile;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;

public class MagneticCoilModel extends AnimatedGeoModel<MagneticCoilTile>
{
	@Override
	public ResourceLocation getAnimationFileLocation(MagneticCoilTile tile) {
		return new ResourceLocation(Techarium.ModID, "animations/magneticcoil.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(MagneticCoilTile tile) {
		return new ResourceLocation(Techarium.ModID, "geo/magneticcoil/magneticcoil.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(MagneticCoilTile tile) {
	
		return new ResourceLocation(Techarium.ModID, "textures/block/animated/magneticcoil/magneticcoil_" + tile.getCoilType().getName() + ".png");
	}
}
