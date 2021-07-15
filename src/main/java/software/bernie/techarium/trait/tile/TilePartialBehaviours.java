package software.bernie.techarium.trait.tile;

import software.bernie.techarium.tile.slaves.TopEnabledOnlySlave;
import software.bernie.techarium.trait.behaviour.PartialBehaviour;
import software.bernie.techarium.trait.block.BlockBehaviour;
import software.bernie.techarium.trait.block.BlockTraits;

public class TilePartialBehaviours {
    public static PartialBehaviour partialEnergyTile = new TileBehaviour.Builder()
            .requiredTraits(TileTraits.PowerTrait.class)
            .partial();
}
