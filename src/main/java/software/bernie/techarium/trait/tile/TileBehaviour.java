package software.bernie.techarium.trait.tile;

import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.trait.behaviour.Behaviour;

public class TileBehaviour extends Behaviour {
    public static class Builder extends Behaviour.Builder<TileBehaviour, TileBehaviour.Builder> {
        public Builder() {
            super(new TileBehaviour());
        }

        public Builder powerTrait(int capacity) {
            return this.with(new TileTraits.PowerTrait(capacity));
        }

        public Builder powerTrait(int capacity, int maxTransfer) {
            return this.with(new TileTraits.PowerTrait(capacity, maxTransfer));
        }

        public Builder powerTrait(int capacity, int maxReceive, int maxExtract) {
            return this.with(new TileTraits.PowerTrait(capacity, maxReceive, maxExtract));
        }

        public Builder powerTrait(int capacity, int maxReceive, int maxExtract, int energy) {
            return this.with(new TileTraits.PowerTrait(capacity, maxReceive, maxExtract, energy));
        }
    }

    public TileBehaviour copy() {
        TileBehaviour.Builder builder = new TileBehaviour.Builder();
        for (Trait trait : this.traits.values()) {
            try {
                builder = builder.with((Trait) trait.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }
}
