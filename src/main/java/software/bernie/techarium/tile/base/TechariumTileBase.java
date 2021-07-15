package software.bernie.techarium.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.trait.behaviour.IHasBehaviour;
import software.bernie.techarium.trait.tile.TileBehaviour;
import software.bernie.techarium.trait.tile.TileTraits;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static software.bernie.techarium.util.StaticHandler.getSideFromDirection;

public abstract class TechariumTileBase extends MachineTileBase implements ITickableTileEntity, IHasBehaviour {
    private final TileBehaviour behaviour;

    public TechariumTileBase(TileEntityType<?> tileEntityTypeIn, TileBehaviour behaviour) {
        super(tileEntityTypeIn);
        this.behaviour = behaviour.copy();
        behaviour.tweak(this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY || cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (level != null && getFaceConfigs().get(getSideFromDirection(side, getFacingDirection())).allowsConnection()) {
                return this.getCapability(cap);
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    private boolean isFirstLoad = true;

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            if(isFirstLoad) {
                updateMachineTile();
                isFirstLoad = false;
            } else if(level.getGameTime() % 3 == 0){
                isFirstLoad = true;
            }
        }
    }

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

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(this.getBlockState(), pkt.getTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.serializeNBT();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.deserializeNBT(tag);
        updateMachineTile();
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

    @Override
    public TileBehaviour getBehaviour() {
        return behaviour;
    }

    public Optional<TileTraits.PowerTrait> getPowerTrait() {
        return behaviour.get(TileTraits.PowerTrait.class);
    }
}
