//package software.bernie.techarium.client.tile.model;
//
//import net.minecraft.util.ResourceLocation;
//import software.bernie.geckolib3.model.AnimatedGeoModel;
//import software.bernie.techarium.Techarium;
//import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
//import software.bernie.techarium.tile.voltaicpile.VoltaicPileTile;
//import software.bernie.techarium.trait.tile.TileTraits;
//
//public class VoltaicPileModel extends AnimatedGeoModel<VoltaicPileTile> {
//    @Override
//    public ResourceLocation getAnimationFileLocation(VoltaicPileTile tile) {
//        return new ResourceLocation(Techarium.ModID, "animations/empty.animation.json");
//    }
//
//    @Override
//    public ResourceLocation getModelLocation(VoltaicPileTile tile) {
//        return new ResourceLocation(Techarium.ModID, "geo/voltaic_pile/voltaic_pile.geo.json");
//    }
//
//    @Override
//    public ResourceLocation getTextureLocation(VoltaicPileTile tile) {
//        if (!tile.getPowerTrait().isPresent()) {
//            return new ResourceLocation(Techarium.ModID, "textures/block/voltaic_pile/voltaicpilefull.png");
//        }
//        TileTraits.PowerTrait trait = tile.getPowerTrait().get();
//        float percentStored = (float) trait.getEnergyStorage().getEnergyStored() / trait.getEnergyStorage().getMaxEnergyStored();
//        if (percentStored <= 0.16) {
//            return new ResourceLocation(Techarium.ModID, "textures/block/voltaic_pile/voltaicpileempty.png");
//        }
//        else if (percentStored <= 0.5) {
//            return new ResourceLocation(Techarium.ModID, "textures/block/voltaic_pile/voltaicpile33.png");
//        }
//        else if (percentStored <= 0.84) {
//            return new ResourceLocation(Techarium.ModID, "textures/block/voltaic_pile/voltaicpile66.png");
//        }
//        return new ResourceLocation(Techarium.ModID, "textures/block/voltaic_pile/voltaicpilefull.png");
//    }
//}
