package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.depot.DepotTileEntity;

public class DepotModel extends AnimatedGeoModel<DepotTileEntity> {
    @Override
    public ResourceLocation getModelLocation(DepotTileEntity tileEntity) {
        return Techarium.rl("geo/depot/depot.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DepotTileEntity tileEntity) {
        return Techarium.rl("textures/block/animated/arboretum.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DepotTileEntity tileEntity) {
        return Techarium.rl("animations/depot.animation.json");
    }
}
