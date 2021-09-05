package software.bernie.techarium.trait.tile;


public class TileBehaviours {
    public static TileBehaviour voltaicPile = new TileBehaviour.Builder()
            .composeFrom(TilePartialBehaviours.partialEnergyTile)
            .with(new TileTraits.PowerTrait(1000, 0, 10, 1000))
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
