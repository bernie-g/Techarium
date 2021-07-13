package software.bernie.techarium.tile.voltaicpile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.display.container.ExchangeStationContainer;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.addon.inventory.InventoryAddon;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.recipe.recipe.ExchangeStationRecipe;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.tile.base.FunctionalTileBase;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.tile.base.MachineTileBase;
import software.bernie.techarium.util.TechariumEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.EXCHANGE_STATION_DRAWABLE;
import static software.bernie.techarium.registry.BlockRegistry.EXCHANGE_STATION;
import static software.bernie.techarium.registry.BlockRegistry.VOLTAIC_PILE;

public class VoltaicPileTile extends FunctionalTileBase {
    private TechariumEnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.of(this::getEnergyStorage);

    public VoltaicPileTile() {
        super(VOLTAIC_PILE.getTileEntityType());
        this.energyStorage = new TechariumEnergyStorage(1000, 10, 10, 1000);
    }

    @Override
    public ActionResultType onTileActivated(PlayerEntity player) {
        return ActionResultType.PASS;
    }

    @Nonnull
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.lazyEnergyStorage.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == CapabilityEnergy.ENERGY) {
            return lazyEnergyStorage.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        energyStorage.deserializeNBT(nbt.getCompound("energy"));
        super.load(state, nbt);
        updateMachineTile();
    }


    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("energy", energyStorage.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void tick() {
        if (level.getDayTime() % 40 == 0 && !level.isClientSide()) {
            for (Direction d : Direction.values()) {
                if (level.getBlockState(worldPosition.relative(d)).getBlock() instanceof FireBlock && getEnergyStorage().getEnergyStored() > 0) {
                    explode();
                }
            }
        }
        super.tick();
    }

    public void explode() {
        level.explode(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), this.getEnergyStorage().getEnergyStored() / 100F, Explosion.Mode.DESTROY);
        level.setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
    }
}