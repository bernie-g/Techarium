package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;

public class MagneticCoilModel extends AnimatedGeoModel<MagneticCoilTile>
{
	@Override
	public ResourceLocation getAnimationFileLocation(MagneticCoilTile tile) {
		return new ResourceLocation(Techarium.MOD_ID, "animations/magneticcoil.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(MagneticCoilTile tile) {
		return new ResourceLocation(Techarium.MOD_ID, "geo/magneticcoil/magneticcoil.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(MagneticCoilTile tile) {
	
		return new ResourceLocation(Techarium.MOD_ID, "textures/block/animated/magneticcoil/magneticcoil_" + tile.getCoilType().getName() + ".png");
	}
}
