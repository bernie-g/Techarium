package software.bernie.techarium.block.base;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
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
import software.bernie.techarium.util.BlockRegion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;

public abstract class MachineBlock<T extends MachineTileBase> extends BaseTileBlock<T> {

    public MachineBlock(Properties properties, Supplier<T> tileSupplier) {
        super(properties,tileSupplier);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ActionResultType result = ActionResultType.SUCCESS;
        if(!world.isClientSide()) {
            if (player instanceof ServerPlayerEntity) {
                handleTileEntity(world, pos, (ServerPlayerEntity) player);
            }
        }
        return result;
    }

    protected void handleTileEntity(IWorld world, BlockPos pos, ServerPlayerEntity player) {
        Optional.ofNullable(world.getBlockEntity(pos))
                .filter(tileEntity -> tileEntity instanceof MachineTileBase)
                .map(tileEntity -> (MachineTileBase)tileEntity)
                .ifPresent(tile -> tile.onTileActivated(player));
    }

    @Deprecated
    @Override
    @SuppressWarnings("deprecared")
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = world.getBlockEntity(pos);
            if(tileentity instanceof MachineMasterTile){
                MachineMasterTile<?> master = (MachineMasterTile<?>) tileentity;
                master.masterHandleDestruction();
            } else if(tileentity instanceof MachineSlaveTile){
                MachineSlaveTile slave = (MachineSlaveTile) tileentity;
                TileEntity masterT = world.getBlockEntity(slave.getMasterPos());
                if(masterT instanceof MachineMasterTile){
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
        if(tileentity instanceof MultiblockMasterTile<?>) {
            MultiblockMasterTile<?> master = (MultiblockMasterTile<?>) tileentity;
            master.placeSlaves();
        }
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state)
    {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean canBePlaced(World world, BlockPos pos) {
        BlockRegion region = getBlockSize();
        for (int x = region.xOff; x < region.xSize - region.xOff; x++) {
            for (int y = region.yOff; y < region.ySize - region.yOff; y++) {
                for (int z = region.zOff; z < region.zSize - region.zOff; z++) {
                    if (!world.getBlockState(pos.offset(x,y,z)).getMaterial().isReplaceable()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public BlockRegion getBlockSize() {
        return BlockRegion.FULL_BLOCK;
    }
}
