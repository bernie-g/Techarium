package software.bernie.techarium.block.pipe;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.item.PipeItem;
import software.bernie.techarium.pipes.capability.IPipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeType;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.tile.pipe.PipeTile;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PipeBlock extends Block {

    public PipeBlock() {
        super(AbstractBlock.Properties.create(Material.ROCK));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PipeTile();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (worldIn.isRemote)
            return;
        if (placer != null && placer.isSneaking()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof PipeTile) {
                ((PipeTile) te).isInput = true;
            }
        }
        if (!worldIn.isRemote())
            handlePlace(state, worldIn, pos, stack);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack activatedWith =  player.getHeldItem(handIn);
        if (activatedWith.getItem() instanceof PipeItem && !worldIn.isRemote && handlePlace(state, worldIn, pos, activatedWith))
            return ActionResultType.CONSUME;
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof PipeTile) {
            PipeTile pipe = ((PipeTile)te);
            if (pipe.isType(PipeType.ITEM))
                return ItemRegistry.ITEM_PIPE.get().getDefaultInstance();
            if (pipe.isType(PipeType.FLUID))
                return ItemRegistry.FLUID_PIPE.get().getDefaultInstance();
            if (pipe.isType(PipeType.ENERGY))
                return ItemRegistry.ENERGY_PIPE.get().getDefaultInstance();
        }
        return ItemRegistry.ITEM_PIPE.get().getDefaultInstance();
    }

    @Override
    public Item asItem() {
        return ItemRegistry.ITEM_PIPE.get();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof PipeTile) {
            PipeTile pipe = ((PipeTile)te);
            if (pipe.isType(PipeType.ITEM))
                return ItemRegistry.ITEM_PIPE.get().getDefaultInstance();
            if (pipe.isType(PipeType.FLUID))
                return ItemRegistry.FLUID_PIPE.get().getDefaultInstance();
            if (pipe.isType(PipeType.ENERGY))
                return ItemRegistry.ENERGY_PIPE.get().getDefaultInstance();
        }
        return ItemRegistry.ITEM_PIPE.get().getDefaultInstance();
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock()))
            handleRemove(world, pos);
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        if (world instanceof ServerWorld) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof PipeTile) {
                ((PipeTile)te).updateDisplayState();
            }
        }
    }

    private static boolean handlePlace(BlockState state, World world, BlockPos pos, ItemStack stack) {
        PipeType type = ((PipeItem)stack.getItem()).getType();
        PipeTile pipeTile = (PipeTile) world.getTileEntity(pos);
        if (pipeTile.isType(type)) {
            return false;
        }
        Map<Direction, UUID> networks = getSurroundingNetworks(world, pos, type);
        LazyOptional<IPipeNetworkManagerCapability> networkManagerCapability = world.getCapability(PipeNetworkManagerCapability.INSTANCE);
        if (!networkManagerCapability.isPresent()) {
            LOGGER.error("Could not find pipe network manager");
            return false;
        }
        IPipeNetworkManagerCapability networkManager = networkManagerCapability.orElseThrow(NullPointerException::new);
        UUID network;
        switch ((int)networks.values().stream().distinct().count()) {
            case 0:
                network = networkManager.createNetwork(type);
                networkManager.appendToNetwork(pos, pipeTile, network);
                break;
            case 1:
                Map.Entry<Direction, UUID> connectedNetwork = networks.entrySet().iterator().next();
                network = connectedNetwork.getValue();
                networkManager.appendToNetwork(pos, pipeTile, connectedNetwork.getValue());
                break;
            default: // multiple
                List<UUID> UUIDs = networks.entrySet().stream().map(Map.Entry::getValue).distinct().collect(Collectors.toList());
                network = networkManager.mergeNetworks((ServerWorld)world, pos, pipeTile, UUIDs);
                break;
        }
        pipeTile.addType(type, network);
        pipeTile.updateDisplayState();
        return true;
    }


    private static void handleRemove(World world, BlockPos pos) {
        PipeTile pipeTile = (PipeTile) world.getTileEntity(pos);
        for (PipeType type: PipeType.values()) {
            if (pipeTile.isType(type))
                handleRemoveForType(pipeTile, world, pos, type);
        }
    }

    private static void handleRemoveForType(PipeTile pipeTile, World world, BlockPos pos, PipeType type) {
        if (!pipeTile.isType(type)) {
            return;
        }
        Map<Direction, UUID> networks = getSurroundingNetworks(world, pos, type);
        LazyOptional<IPipeNetworkManagerCapability> networkManagerCapability = world.getCapability(PipeNetworkManagerCapability.INSTANCE);
        if (!networkManagerCapability.isPresent()) {
            LOGGER.error("Could not find pipe network manager");
            return;
        }
        IPipeNetworkManagerCapability networkManager = networkManagerCapability.orElseThrow(NullPointerException::new);
        UUID network = pipeTile.getNetworkUUID(type);
        switch (networks.size()) {
            case 0:
                networkManager.deleteNetwork(network);
                break;
            case 1:
                networkManager.removeFromNetwork(pos, network);
                break;
            default: // multiple
                networkManager.removeFromNetwork(pos, network);
                networkManager.splitNetwork(pos,networks.keySet(), network);
        }
    }

    public static Map<Direction, UUID> getSurroundingNetworks(World world, BlockPos pos, PipeType type) {
        EnumMap<Direction, UUID> networks = new EnumMap<>(Direction.class);
        for (Direction direction: Direction.values()) {
            TileEntity te = world.getTileEntity(pos.offset(direction));
            if (te instanceof PipeTile && ((PipeTile) te).isType(type)) {
                networks.put(direction, ((PipeTile) te).getNetworkUUID(type));
            }
        }
        return networks;
    }
}
