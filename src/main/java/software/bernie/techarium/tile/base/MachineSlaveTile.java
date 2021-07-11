package software.bernie.techarium.tile.base;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.data.DataHolder;
import software.bernie.techarium.trait.block.MasterBlockTrait;
import software.bernie.techarium.util.SlaveSpot;
import software.bernie.techarium.util.TechariumCodecs;
import software.bernie.techarium.util.Utils;
import software.bernie.techarium.util.VectorUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static software.bernie.techarium.util.StaticHandler.getSideFromDirection;

public class MachineSlaveTile extends MachineTileBase {

    @Getter
    public final DataHolder<Vector3i> masterOffset = createDataHolder("masterOffset", TechariumCodecs.VECTOR3I);

    @Setter
    private VoxelShape boundingBox;

    @Setter
    private VoxelShape collisionBox;

    public MachineSlaveTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public boolean isBeingDestroyed = false;

    @Override
    public ActionResultType onTileActivated(PlayerEntity player) {
        assert level != null;
        return ((MachineTileBase) Objects.requireNonNull(this.getMasterTile())).onTileActivated(player);
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (shouldGetCapabilityFromMaster(cap) && level != null && getFaceConfigs()
                .get(getSideFromDirection(side, getFacingDirection())).allowsConnection())
            return Objects.requireNonNull(this.getMasterTile()).getCapability(cap);
        return super.getCapability(cap, side);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (shouldGetCapabilityFromMaster(cap))
            return Objects.requireNonNull(this.getMasterTile()).getCapability(cap);
        return super.getCapability(cap);
    }

    private boolean shouldGetCapabilityFromMaster(@NotNull Capability<?> cap) {
        return getMasterOffset()
                .get() != BlockPos.ZERO && (cap == CapabilityEnergy.ENERGY || cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

    public TileEntity getMasterTile() {
        if(masterOffset.get() == null){
            return null;
        }
        return level.getBlockEntity(this.getBlockPos().offset(masterOffset.get()));
    }

    public VoxelShape getBoundingBox() {
        if (boundingBox == null) {
            TileEntity masterTile = this.getMasterTile();
            if (masterTile instanceof TechariumTileEntity) {
                boundingBox = getSlaveSpot(masterTile, false).map(SlaveSpot::getVoxelShape).orElse(null);
                if (boundingBox != null && masterTile.getBlockState().hasProperty(HorizontalBlock.FACING)) {
                    boundingBox = Utils
                            .rotateVoxelShape(boundingBox, masterTile.getBlockState().getValue(HorizontalBlock.FACING));
                }
            }
        }
        return boundingBox;
    }

    public VoxelShape getCollisionBox() {
        if (collisionBox == null) {
            TileEntity masterTile = this.getMasterTile();
            if (masterTile instanceof TechariumTileEntity) {
                collisionBox = getSlaveSpot(masterTile, true).map(SlaveSpot::getVoxelShape).orElse(null);
                if (collisionBox != null && masterTile.getBlockState().hasProperty(HorizontalBlock.FACING)) {
                    collisionBox = Utils
                            .rotateVoxelShape(collisionBox, masterTile.getBlockState().getValue(HorizontalBlock.FACING));
                }
            }
        }
        return collisionBox;
    }

    private Optional<SlaveSpot> getSlaveSpot(TileEntity masterTile, boolean collision) {
        TechariumBlock block = (TechariumBlock) masterTile.getBlockState().getBlock();
        MasterBlockTrait masterBlockTrait = block.getBehaviour().getRequired(MasterBlockTrait.class);
        Vector3i offset = VectorUtils.mul(this.getMasterOffset().get(), -1);
        if (masterTile.getBlockState().hasProperty(HorizontalBlock.FACING)) {
            Direction facing = masterTile.getBlockState().getValue(HorizontalBlock.FACING);
            if(facing == Direction.NORTH || facing == Direction.SOUTH) facing = facing.getOpposite();
            offset = VectorUtils.rotateVector2D(offset, facing);
        }
        Vector3i finalOffset = offset;
        if (collision) {
            return masterBlockTrait.getSlaveSpots().stream()
                    .filter(slaveSpot -> slaveSpot.getOffset().equals(finalOffset)).findFirst();
        } else {
            return masterBlockTrait.getSlaveCollisions().stream()
                    .filter(slaveSpot -> slaveSpot.getOffset().equals(finalOffset)).findFirst();
        }
    }
}
