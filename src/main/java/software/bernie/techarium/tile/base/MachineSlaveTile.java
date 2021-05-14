package software.bernie.techarium.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static software.bernie.techarium.util.StaticHandler.getSideFromDirection;

public class MachineSlaveTile extends MachineTileBase {

    private BlockPos masterPos = BlockPos.ZERO;

    public MachineSlaveTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    @Override
    public ActionResultType onTileActivated(PlayerEntity player) {
        assert world != null;
        return ((MachineTileBase) Objects.requireNonNull(world.getTileEntity(masterPos))).onTileActivated(player);
    }

    public BlockPos getMasterPos() {
        return masterPos;
    }

    public void setMasterPos(BlockPos masterPos) {
        this.masterPos = masterPos;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (shouldGetCapabilityFromMaster(cap) && world != null && getFaceConfigs().get(getSideFromDirection(side,getFacingDirection())).allowsConnection())
            return Objects.requireNonNull(world.getTileEntity(masterPos)).getCapability(cap);
        return super.getCapability(cap, side);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (shouldGetCapabilityFromMaster(cap))
            return Objects.requireNonNull(world.getTileEntity(masterPos)).getCapability(cap);
        return super.getCapability(cap);
    }

    private boolean shouldGetCapabilityFromMaster(@NotNull Capability<?> cap) {
        return getMasterPos() != BlockPos.ZERO && (cap == CapabilityEnergy.ENERGY || cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(this.getBlockState(), pkt.getNbtCompound());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.serializeNBT();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.deserializeNBT(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        masterPos = BlockPos.fromLong(nbt.getLong("masterPos"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putLong("masterPos",this.masterPos.toLong());
        return super.write(compound);
    }
}
