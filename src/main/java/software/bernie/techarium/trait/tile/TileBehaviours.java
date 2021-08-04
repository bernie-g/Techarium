package software.bernie.techarium.trait.tile;

import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.tile.arboretum.ArboretumTile;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
import software.bernie.techarium.tile.voltaicpile.VoltaicPileTile;
import software.bernie.techarium.trait.block.BlockBehaviour;
import software.bernie.techarium.trait.block.BlockTraits;

import java.util.HashMap;
import java.util.Map;

public class TileBehaviours {
    private static Map<Side, TileTraits.PowerManipulationTrait.ManipulationConfig> voltaicPileConfig =
            new HashMap<Side, TileTraits.PowerManipulationTrait.ManipulationConfig>() {{
                put(Side.FRONT, TileTraits.PowerManipulationTrait.ManipulationConfig.PUSH);
                put(Side.BACK, TileTraits.PowerManipulationTrait.ManipulationConfig.PUSH);
                put(Side.LEFT, TileTraits.PowerManipulationTrait.ManipulationConfig.PUSH);
                put(Side.RIGHT, TileTraits.PowerManipulationTrait.ManipulationConfig.PUSH);
    }};

    public static TileBehaviour voltaicPile = new TileBehaviour.Builder()
            .composeFrom(TilePartialBehaviours.partialEnergyTile)
            .with(new TileTraits.PowerTrait(1000, 0, 10, 1000))
            .with(new TileTraits.PowerManipulationTrait(voltaicPileConfig))
            .build();

    public static TileBehaviour gravMagnet = new TileBehaviour.Builder()
            .composeFrom(TilePartialBehaviours.partialBaseTile)
            .build();

    public static TileBehaviour magneticCoil = new TileBehaviour.Builder()
            .composeFrom(TilePartialBehaviours.partialBaseTile)
            .build();

    public static TileBehaviour base = new TileBehaviour.Builder()
            .composeFrom(TilePartialBehaviours.partialBaseTile)
            .build();
}
