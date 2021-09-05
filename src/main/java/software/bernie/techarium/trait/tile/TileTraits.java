package software.bernie.techarium.trait.tile;

import lombok.Data;
import lombok.SneakyThrows;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.tile.base.MachineTileBase;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.util.TechariumEnergyStorage;
import java.util.Map;

import static software.bernie.techarium.util.StaticHandler.getSideFromDirection;
public class TileTraits {

    @Data
    public static class PowerTrait extends Trait {
        private TechariumEnergyStorage energyStorage;
        private final LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.of(() -> this.energyStorage);

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

        public PowerTrait(TechariumEnergyStorage storage) {
            energyStorage = storage;
        }

        @Override
        @SneakyThrows
        public Object clone() {
            return new PowerTrait(this.getEnergyStorage().copy());
        }
    }
}
