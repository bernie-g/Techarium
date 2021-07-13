package software.bernie.techarium.client.tile.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
import software.bernie.techarium.tile.voltaicpile.VoltaicPileTile;

public class VoltaicPileModel extends AnimatedGeoModel<VoltaicPileTile> {
    @Override
    public ResourceLocation getAnimationFileLocation(VoltaicPileTile tile) {
        return new ResourceLocation(Techarium.ModID, "animations/empty.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(VoltaicPileTile tile) {
        return new ResourceLocation(Techarium.ModID, "geo/voltaic_pile/voltaic_pile.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(VoltaicPileTile tile) {
        int stored = tile.getEnergyStorage().getEnergyStored();
        if (stored <= 160) {
            return new ResourceLocation(Techarium.ModID, "textures/block/voltaic_pile/voltaicpileempty.png");
        }
        else if (stored <= 500) {
            return new ResourceLocation(Techarium.ModID, "textures/block/voltaic_pile/voltaicpile33.png");
        }
        else if (stored <= 840) {
            return new ResourceLocation(Techarium.ModID, "textures/block/voltaic_pile/voltaicpile66.png");
        }
        return new ResourceLocation(Techarium.ModID, "textures/block/voltaic_pile/voltaicpilefull.png");
    }
}
