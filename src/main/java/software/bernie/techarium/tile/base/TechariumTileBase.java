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
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.trait.tile.TileBehaviour;
import software.bernie.techarium.trait.tile.TileTraits;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static software.bernie.techarium.util.StaticHandler.getSideFromDirection;

public abstract class TechariumTileBase extends MachineTileBase implements ITickableTileEntity {
    public TechariumTileBase(TileEntityType<?> tileEntityTypeIn, TileBehaviour behaviour) {
        super(tileEntityTypeIn, behaviour);
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
        getPowerTrait().ifPresent(trait -> {
            for (Direction d : Direction.values()) {
                FaceConfig faceConfig = getFaceConfigs().get(getSideFromDirection(d, getFacingDirection()));
                switch (faceConfig) {
                    case PUSH_ONLY:
                        trait.getEnergyStorage().outputToSide(getLevel(), getBlockPos(), d, Integer.MAX_VALUE);
                    case PULL_ONLY:
                        trait.getEnergyStorage().inputFromSide(getLevel(), getBlockPos(), d, Integer.MAX_VALUE);
                }
            }
        });
        if (!level.isClientSide()) {
            if(isFirstLoad) {
                updateMachineTile();
                isFirstLoad = false;
            } else if(level.getGameTime() % 3 == 0){
                isFirstLoad = true;
            }
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
}
