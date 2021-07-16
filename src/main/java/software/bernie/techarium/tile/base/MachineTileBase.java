package software.bernie.techarium.tile.base;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.trait.block.BlockTraits;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.trait.behaviour.IHasBehaviour;
import software.bernie.techarium.trait.tile.TileBehaviour;
import software.bernie.techarium.trait.tile.TileBehaviours;
import software.bernie.techarium.trait.tile.TileTraits;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static software.bernie.techarium.block.base.RotatableBlock.FACING;

public abstract class MachineTileBase extends TileEntity implements IHasBehaviour {
    private final TileBehaviour behaviour;
    private final Map<Side, FaceConfig> sideFaceConfigs = setFaceControl();

    public MachineTileBase(TileEntityType<?> tileEntityTypeIn, TileBehaviour behaviour) {
        super(tileEntityTypeIn);
        this.behaviour = behaviour.copy();
        behaviour.tweak(this);
    }

    protected Map<Side, FaceConfig> setFaceControl(){
        Map<Side, FaceConfig> defaultSetUp = new HashMap<>();
        for (Side side: Side.values()){
            defaultSetUp.put(side,FaceConfig.NONE);
        }
        return defaultSetUp;
    }

    public Direction getFacingDirection() {
        assert this.level != null;
        BlockState state = this.level.getBlockState(this.worldPosition);
        if (state.getBlock() instanceof TechariumBlock) {
            DirectionProperty direction = ((TechariumBlock<?>) state.getBlock()).getBehaviour().getRequired(BlockTraits.BlockRotationTrait.class).getDirectionProperty();
            return state.getValue(direction);
        }
        Techarium.LOGGER.info("Machine tile did not have a MachineBlock!");
        return Direction.NORTH;
    }

    public Map<Side, FaceConfig> getFaceConfigs() {
        return sideFaceConfigs;
    }

    public abstract ActionResultType onTileActivated(PlayerEntity player);

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        if (behaviour.has(TileTraits.PowerTrait.class)) {
            behaviour.getRequired(TileTraits.PowerTrait.class).getEnergyStorage().deserializeNBT(nbt.getCompound("energy"));
        }
        super.load(state, nbt);
        updateMachineTile();
    }


    @Override
    public CompoundNBT save(CompoundNBT compound) {
        if (behaviour.has(TileTraits.PowerTrait.class)) {
            compound.put("energy", behaviour.getRequired(TileTraits.PowerTrait.class).getEnergyStorage().serializeNBT());
        }
        return super.save(compound);
    }

    protected void updateMachineTile() {
        requestModelDataUpdate();
        this.setChanged();
        if (this.getLevel() != null) {
            this.getLevel().sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        if (behaviour.has(TileTraits.PowerTrait.class)) {
            behaviour.getRequired(TileTraits.PowerTrait.class).getLazyEnergyStorage().invalidate();
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == CapabilityEnergy.ENERGY && behaviour.has(TileTraits.PowerTrait.class)) {
            return behaviour.getRequired(TileTraits.PowerTrait.class).getLazyEnergyStorage().cast();
        }
        return super.getCapability(cap);
    }

    public Optional<TileTraits.PowerTrait> getPowerTrait() {
        return behaviour.get(TileTraits.PowerTrait.class);
    }

    @Override
    public TileBehaviour getBehaviour() {
        return behaviour;
    }
}
