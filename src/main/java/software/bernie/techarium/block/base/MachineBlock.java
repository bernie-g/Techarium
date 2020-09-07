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
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.techarium.tile.base.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Supplier;

import static software.bernie.techarium.registry.ItemRegistry.DEBUGSTICK;

public abstract class MachineBlock<T extends MachineTile> extends Block {

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
        ActionResultType result = ActionResultType.FAIL;
        if(player instanceof ServerPlayerEntity && !player.getActiveItemStack().getItem().equals(DEBUGSTICK.get())) {
            handleTileEntity(world, pos, (ServerPlayerEntity) player);
            result = ActionResultType.PASS;
        }

        return result;
    }

    protected void handleTileEntity(IWorld world, BlockPos pos, ServerPlayerEntity player) {
        Optional.ofNullable(world.getTileEntity(pos))
                .filter(tileEntity -> tileEntity instanceof MachineTile)
                .map(tileEntity -> (MachineTile)tileEntity)
                .ifPresent(tile -> NetworkHooks.openGui(player,tile,packetBuffer -> {
                    packetBuffer.writeBlockPos(tile.getPos());
                    packetBuffer.writeTextComponent(tile.getDisplayName());
                }));
    }
}
