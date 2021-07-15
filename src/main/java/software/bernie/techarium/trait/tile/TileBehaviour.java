package software.bernie.techarium.trait.tile;

import net.minecraft.block.BlockRenderType;
import net.minecraft.tileentity.TileEntity;
import software.bernie.techarium.registry.lang.LangEntry;
import software.bernie.techarium.trait.Traits;
import software.bernie.techarium.trait.behaviour.Behaviour;
import software.bernie.techarium.trait.block.BlockTraits;
import software.bernie.techarium.util.TechariumEnergyStorage;

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
}
