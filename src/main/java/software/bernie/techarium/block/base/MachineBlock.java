package software.bernie.techarium.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import software.bernie.techarium.block.MachineSlave;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.tile.base.MachineSlaveTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class MachineBlock<T extends MachineMasterTile> extends RotatableBlock {

    private final Supplier<T> tileSupplier;

    public MachineBlock(Properties properties, Supplier<T> tileSupplier) {
        super(properties);
        this.tileSupplier = tileSupplier;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return this.tileSupplier.get();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ActionResultType result = ActionResultType.SUCCESS;
        if(!world.isRemote()) {
            if (player instanceof ServerPlayerEntity) {
                handleTileEntity(world, pos, (ServerPlayerEntity) player);
            }
        }
        return result;
    }

    protected void handleTileEntity(IWorld world, BlockPos pos, ServerPlayerEntity player) {
        Optional.ofNullable(world.getTileEntity(pos))
                .filter(tileEntity -> tileEntity instanceof MachineMasterTile)
                .map(tileEntity -> (MachineMasterTile<?>)tileEntity)
                .ifPresent(tile -> tile.onTileActicated(player));
    }

    @Deprecated
    @Override
    @SuppressWarnings("deprecared")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if(tileentity instanceof MachineMasterTile){
                MachineMasterTile<?> master = (MachineMasterTile<?>) tileentity;
                master.masterHandleDestruction();
            } else if(tileentity instanceof MachineSlaveTile){
                MachineSlaveTile slave = (MachineSlaveTile) tileentity;
                TileEntity masterT = worldIn.getTileEntity(slave.getMasterPos());
                if(masterT instanceof MachineMasterTile){
                    MachineMasterTile<?> master = (MachineMasterTile<?>) masterT;
                    master.masterHandleDestruction();
                }
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
