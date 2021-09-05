package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;

public class ExchangeStationModel extends AnimatedGeoModel<ExchangeStationTile> {
    @Override
    public ResourceLocation getAnimationFileLocation(ExchangeStationTile tile) {
        return new ResourceLocation(Techarium.MOD_ID, "animations/exchangestation.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(ExchangeStationTile tile) {
        return new ResourceLocation(Techarium.MOD_ID, "geo/exchange_station/exchange_station.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ExchangeStationTile tile) {
        return new ResourceLocation(Techarium.MOD_ID, "textures/block/animated/exchange_station.png");
    }
}
