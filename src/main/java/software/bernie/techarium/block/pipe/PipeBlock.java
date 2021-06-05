package software.bernie.techarium.block.pipe;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
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
import software.bernie.techarium.util.Utils;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PipeBlock extends Block {

    private static final VoxelShape SOUTH_END = box(4,4,15,12,12,16);
    private static final VoxelShape SOUTH_PIPE = box(6,6,8,10,10,15);


    public PipeBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).noOcclusion());
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
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (worldIn.isClientSide)
            return;
        if (placer != null && placer.isShiftKeyDown()) {
            TileEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof PipeTile) {
                ((PipeTile) te).isInput = true;
            }
        }
        if (!worldIn.isClientSide())
            handlePlace(state, worldIn, pos, stack);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack activatedWith =  player.getItemInHand(handIn);
        if (activatedWith.getItem() instanceof PipeItem && !worldIn.isClientSide && handlePlace(state, worldIn, pos, activatedWith))
            return ActionResultType.CONSUME;
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
        TileEntity te = worldIn.getBlockEntity(pos);
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
        TileEntity te = world.getBlockEntity(pos);
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
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()))
            handleRemove(world, pos);
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        if (world instanceof ServerWorld) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof PipeTile) {
                ((PipeTile)te).updateDisplayState();
            }
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return getShape(state, reader, pos, ISelectionContext.empty());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof PipeTile) {
            PipeTile pipeTile = (PipeTile) te;
            PipeData data = pipeTile.getDisplayData();
            VoxelShape shape = VoxelShapes.empty();
            for (Direction direction: Direction.values()) {
                if (data.pipeEnds.containsKey(direction) && data.pipeEnds.get(direction))
                    shape = VoxelShapes.or(shape, Utils.rotateVoxelShape(SOUTH_END, direction));
                if (data.pipeConnections.containsKey(direction))
                    shape = VoxelShapes.or(shape, Utils.rotateVoxelShape(SOUTH_PIPE, direction));
            }
            return shape;
        }
        return super.getShape(state, worldIn, pos, context);
    }

    private static boolean handlePlace(BlockState state, World world, BlockPos pos, ItemStack stack) {
        PipeType type = ((PipeItem)stack.getItem()).getType();
        PipeTile pipeTile = (PipeTile) world.getBlockEntity(pos);
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
        PipeTile pipeTile = (PipeTile) world.getBlockEntity(pos);
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
            TileEntity te = world.getBlockEntity(pos.relative(direction));
            if (te instanceof PipeTile && ((PipeTile) te).isType(type)) {
                networks.put(direction, ((PipeTile) te).getNetworkUUID(type));
            }
        }
        return networks;
    }
}
