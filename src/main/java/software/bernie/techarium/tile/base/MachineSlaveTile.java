package software.bernie.techarium.tile.base;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.data.DataHolder;
import software.bernie.techarium.util.TechariumCodecs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static software.bernie.techarium.util.StaticHandler.getSideFromDirection;

public class MachineSlaveTile extends MachineTileBase {

    @Getter
    public final DataHolder<Vector3i> masterOffset = createDataHolder("masterOffset", TechariumCodecs.VECTOR3I);

    public MachineSlaveTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public ActionResultType onTileActivated(PlayerEntity player) {
        assert level != null;
        return ((MachineTileBase) Objects.requireNonNull(this.getMasterTile())).onTileActivated(player);
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (shouldGetCapabilityFromMaster(cap) && level != null && getFaceConfigs().get(getSideFromDirection(side,getFacingDirection())).allowsConnection())
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
        return getMasterOffset().get() != BlockPos.ZERO && (cap == CapabilityEnergy.ENERGY || cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

    public TileEntity getMasterTile(){
        return level.getBlockEntity(this.getBlockPos().offset(masterOffset.get()));
    }
}
