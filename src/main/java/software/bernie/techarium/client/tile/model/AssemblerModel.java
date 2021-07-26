package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.assembler.AssemblerTile;

public class AssemblerModel extends AnimatedGeoModel<AssemblerTile>
{
	@Override
	public ResourceLocation getAnimationFileLocation(AssemblerTile tile) {
		return new ResourceLocation(Techarium.ModID, "animations/assembler.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(AssemblerTile tile) {
		return new ResourceLocation(Techarium.ModID, "geo/assembler/assembler.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(AssemblerTile tile) {
		return new ResourceLocation(Techarium.ModID, "textures/block/animated/assembler.png");
	}
}
