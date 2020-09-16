package software.bernie.techarium.tile.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static software.bernie.techarium.block.base.RotatableBlock.FACING;
import static software.bernie.techarium.util.StaticHandler.getSideFromDirection;

public class MachineSlaveTile extends MachineTileBase {

    private BlockPos masterPos = BlockPos.ZERO;

    public MachineSlaveTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    @Override
    public ActionResultType onTileActicated(PlayerEntity player) {
        assert world != null;
        return ((MachineTileBase) Objects.requireNonNull(world.getTileEntity(masterPos))).onTileActicated(player);
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
        if(getMasterPos() != BlockPos.ZERO) {
            if (cap == CapabilityEnergy.ENERGY || cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                if (world != null && getFaceConfigs().get(getSideFromDirection(side,getFacingDirection())).allowsConnection()) {
                    return Objects.requireNonNull(world.getTileEntity(masterPos)).getCapability(cap);
                }
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void read(CompoundNBT compound) {
        masterPos = BlockPos.fromLong(compound.getLong("masterPos"));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putLong("masterPos",this.masterPos.toLong());
        return super.write(compound);
    }
}
