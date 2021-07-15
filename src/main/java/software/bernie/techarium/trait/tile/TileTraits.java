package software.bernie.techarium.trait.tile;

import lombok.Data;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.util.TechariumEnergyStorage;

public class TileTraits {
    // TODO: ADD FLUID TRAIT

    @Data
    public static class PowerTrait extends Trait {
        private TechariumEnergyStorage energyStorage;
        private final LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.of(this::getEnergyStorage);

        public PowerTrait(int capacity) {
            energyStorage = new TechariumEnergyStorage(capacity);
        }

        public PowerTrait(int capacity, int maxTransfer) {
            energyStorage = new TechariumEnergyStorage(capacity, maxTransfer);
        }

        public PowerTrait(int capacity, int maxReceive, int maxExtract) {
            energyStorage = new TechariumEnergyStorage(capacity, maxReceive, maxExtract);
        }

        public PowerTrait(int capacity, int maxReceive, int maxExtract, int energy) {
            energyStorage = new TechariumEnergyStorage(capacity, maxReceive, maxExtract, energy);
        }
    }
}
