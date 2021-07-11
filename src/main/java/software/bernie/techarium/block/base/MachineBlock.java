package software.bernie.techarium.block.base;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import software.bernie.techarium.tile.base.MachineTileBase;
import software.bernie.techarium.trait.block.BlockBehaviour;
import software.bernie.techarium.util.BlockRegion;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public abstract class MachineBlock<T extends MachineTileBase> extends TechariumBlock {

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



    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean canBePlaced(World world, BlockPos pos) {
        BlockRegion region = getBlockSize();
        for (int x = region.xOff; x < region.xSize - region.xOff; x++) {
            for (int y = region.yOff; y < region.ySize - region.yOff; y++) {
                for (int z = region.zOff; z < region.zSize - region.zOff; z++) {
                    if (!world.getBlockState(pos.offset(x, y, z)).getMaterial().isReplaceable()) {
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
