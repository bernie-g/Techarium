package software.bernie.techarium.block.pipe;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.pipes.capability.IPipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeNetworkManagerCapability;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.tile.pipe.PipeTileEntity;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class PipeBlock extends Block {
    public PipeBlock() {
        super(AbstractBlock.Properties.create(Material.ROCK));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (worldIn.isRemote)
            return;
        if (placer != null && placer.isSneaking()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof PipeTileEntity) {
                ((PipeTileEntity) te).isInput = true;
            }
        }
        handlePlace(state, worldIn, pos, stack);
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return ItemRegistry.ITEM_PIPE.get().getDefaultInstance();
    }

    @Override
    public Item asItem() {
        return ItemRegistry.ITEM_PIPE.get();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return ItemRegistry.ITEM_PIPE.get().getDefaultInstance();
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    private static void handlePlace(BlockState state, World world, BlockPos pos, ItemStack stack) {
        EnumMap<Direction, UUID> networks = new EnumMap<>(Direction.class);
        for (Direction direction: Direction.values()) {
            TileEntity te = world.getTileEntity(pos.offset(direction));
            if (te instanceof PipeTileEntity) {
                networks.put(direction, ((PipeTileEntity) te).network);
            }
        }
        LazyOptional<IPipeNetworkManagerCapability> networkManagerCapability = world.getCapability(PipeNetworkManagerCapability.INSTANCE);
        if (!networkManagerCapability.isPresent())
            return;
        IPipeNetworkManagerCapability networkManager = networkManagerCapability.orElseThrow(NullPointerException::new);
        PipeTileEntity pipeTileEntity = (PipeTileEntity) world.getTileEntity(pos);
        switch (networks.size()) {
            case 0:
                pipeTileEntity.network = networkManager.createNetwork(pos, pipeTileEntity);
                break;
            case 1:
                Map.Entry<Direction, UUID> connectedNetwork = networks.entrySet().iterator().next();
                pipeTileEntity.network = connectedNetwork.getValue();
                networkManager.appendToNetwork(pos, pipeTileEntity, connectedNetwork.getValue(), connectedNetwork.getKey());
                break;

            default: // multiple

        }
    }

    private static void handleRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState) {

    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PipeTileEntity();
    }
}
