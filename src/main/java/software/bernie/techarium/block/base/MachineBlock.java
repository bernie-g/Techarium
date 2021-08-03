package software.bernie.techarium.block.base;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.tile.base.MachineSlaveTile;
import software.bernie.techarium.tile.base.MachineTileBase;
import software.bernie.techarium.tile.base.MultiblockMasterTile;
import software.bernie.techarium.trait.block.BlockBehaviour;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public abstract class MachineBlock<T extends MachineTileBase> extends TechariumBlock<T> {

    public MachineBlock(BlockBehaviour behaviour, Properties properties) {
        super(behaviour, properties);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ActionResultType result = ActionResultType.SUCCESS;
        if (!world.isClientSide()) {
            if (player instanceof ServerPlayerEntity) {
                handleTileEntity(world, pos, (ServerPlayerEntity) player);
            }
        }
        return result;
    }

    protected void handleTileEntity(IWorld world, BlockPos pos, ServerPlayerEntity player) {
        Optional.ofNullable(world.getBlockEntity(pos))
                .filter(tileEntity -> tileEntity instanceof MachineTileBase)
                .map(tileEntity -> (MachineTileBase) tileEntity)
                .ifPresent(tile -> tile.onTileActivated(player));
    }

    @Deprecated
    @Override
    @SuppressWarnings("deprecared")
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof MachineMasterTile) {
                MachineMasterTile<?> master = (MachineMasterTile<?>) tileentity;
                master.masterHandleDestruction();
            } else if (tileentity instanceof MachineSlaveTile) {
                MachineSlaveTile slave = (MachineSlaveTile) tileentity;
                TileEntity masterT = world.getBlockEntity(slave.getMasterPos());
                if (masterT instanceof MachineMasterTile) {
                    MachineMasterTile<?> master = (MachineMasterTile<?>) masterT;
                    master.masterHandleDestruction();
                }
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof MultiblockMasterTile<?>) {
            MultiblockMasterTile<?> master = (MultiblockMasterTile<?>) tileentity;
            master.placeSlaves();
        }
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
