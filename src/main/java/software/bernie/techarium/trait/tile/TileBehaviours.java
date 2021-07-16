package software.bernie.techarium.trait.tile;

import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
import software.bernie.techarium.tile.voltaicpile.VoltaicPileTile;
import software.bernie.techarium.trait.block.BlockBehaviour;
import software.bernie.techarium.trait.block.BlockTraits;

public class TileBehaviours {
    public static TileBehaviour voltaicPile = new TileBehaviour.Builder()
            .composeFrom(TilePartialBehaviours.partialEnergyTile)
            .with(new TileTraits.PowerTrait(1000, 0, 10, 1000))
            .build();

    public static TileBehaviour base = new TileBehaviour.Builder()
            .composeFrom(TilePartialBehaviours.partialBaseTile)
            .build();
}
