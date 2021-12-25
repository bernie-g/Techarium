package software.bernie.techarium.block.electrochromicglass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FourWayBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.ServerWorldLightManager;
import net.minecraftforge.fml.network.NetworkDirection;
import software.bernie.techarium.config.TechariumConfig;
import software.bernie.techarium.network.NetworkConnection;
import software.bernie.techarium.network.block.ElectroChromicGlassPacket;

public class ElectroChromicGlassBlock extends Block {

	public static final int RADIUS = TechariumConfig.SERVER_CONFIG.electroChromicGlassRadius.get();
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final IntegerProperty POWER   = IntegerProperty.create("power", 0, RADIUS);
	
	public ElectroChromicGlassBlock(Properties p_i48373_1_) {
		super(p_i48373_1_);
		this.registerDefaultState(this.defaultBlockState().setValue(POWERED, Boolean.valueOf(false)).setValue(POWER, 0));
		
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext useContext) {
		BlockState state = super.getStateForPlacement(useContext);
		World world = useContext.getLevel();
		BlockPos pos = useContext.getClickedPos();
		
		for (Direction dir : Direction.values()) {
			BlockState offsetState = world.getBlockState(pos.relative(dir));
			if (offsetState.getBlock() == this) {
				if (offsetState.getValue(POWERED) && offsetState.getValue(POWER) > 0) {
					int power = offsetState.getValue(POWER) - 1;
					
					sendSignal(world, pos, state, power, true, false);
					return state.setValue(POWERED, Boolean.valueOf(true)).setValue(POWER, power);					
				}
			}
		}
		
		return state;
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWERED, POWER);
		super.createBlockStateDefinition(builder);
	}
	
	public void neighborChanged(BlockState blockState, World world, BlockPos pos, Block block, BlockPos pos2, boolean bool) {
		if (!world.isClientSide) {
			if (world.hasNeighborSignal(pos) && block.isSignalSource(world.getBlockState(pos2))) {
				sendSignal(world, pos, blockState, RADIUS, !blockState.getValue(POWERED), true);
			}
		}
	}
	
	public void sendSignal(World world, BlockPos pos, BlockState state, int power, boolean powered, boolean shouldUpdate) {
		if (state.getBlock() != this)
			return;
		
		if (shouldUpdate)
			world.setBlock(pos, state.setValue(POWERED, powered).setValue(POWER, power), 18);
		
		if (power <= 0)
			return;
		
		if (world.getBlockState(pos.relative(Direction.EAST)).getBlock() == this)
			updateDirection(world, pos, power, powered, Direction.EAST);
		
		if (world.getBlockState(pos.relative(Direction.WEST)).getBlock() == this)
			updateDirection(world, pos, power, powered, Direction.WEST);
		
		if (world.getBlockState(pos.relative(Direction.SOUTH)).getBlock() == this)
			updateDirection(world, pos, power, powered, Direction.SOUTH);
		
		if (world.getBlockState(pos.relative(Direction.NORTH)).getBlock() == this)
			updateDirection(world, pos, power, powered, Direction.NORTH);

		if (world.getBlockState(pos.relative(Direction.UP)).getBlock() == this)
			updateDirection(world, pos, power, powered, Direction.UP);
		
		if (world.getBlockState(pos.relative(Direction.DOWN)).getBlock() == this)
			updateDirection(world, pos, power, powered, Direction.DOWN);
	}
	
	private void updateDirection(World world, BlockPos pos, int power, boolean powered, Direction dir) {
		BlockPos posOffset = pos.relative(dir);
		BlockState state  = world.getBlockState(posOffset); 
		
		if (state.getBlock() != this)
			return;
		
		if (state.getValue(POWERED) != powered)
			sendSignal(world, posOffset, state, power-1, powered, true);
		
		else if (state.getValue(POWER) < power - 1)
			sendSignal(world, posOffset, state, power-1, powered, true);
	}
	
	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState,
			boolean bool) {
		if (!world.isClientSide()) {
			if (newState.getBlock() != this) {
				if (state.getValue(POWERED) && state.getValue(POWER) == RADIUS)
					sendSignal(world, pos, state, RADIUS, false, false);
				else sendDestroySignal(new ArrayList<BlockPos>(), world, state, pos, pos);
			}
		}
			
		super.onRemove(state, world, pos, newState, bool);
	}
	
	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (!world.isClientSide()) {			
			if (state.getValue(POWER) == RADIUS) {
				sendSignal(world, pos, state, RADIUS, true, true);
			}
			
			else if (!state.getValue(POWERED)) {
				for (PlayerEntity player : world.players()) {
					ElectroChromicGlassPacket packet = new ElectroChromicGlassPacket(pos);
					NetworkConnection.INSTANCE.sendTo(packet, ((ServerPlayerEntity) player).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
				}
			}
		}
		super.tick(state, world, pos, rand);
	}
	
	@Override
	public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
		return PushReaction.BLOCK;
	}
	
	private void sendDestroySignal(List<BlockPos> posDone, World world, BlockState state, BlockPos pos, BlockPos start) {		
		if (state.getBlock() != this)
			return;
		if (!state.getValue(POWERED))
			return;
		
		if (state.getValue(POWER) == RADIUS) {
			if (!pos.equals(start)) 
				world.setBlock(pos, state.setValue(POWER, RADIUS), 18);
			
			world.getBlockTicks().scheduleTick(pos, this, 4);
			return;
		}
					
		if (world.getBlockState(pos.relative(Direction.EAST)).getBlock() == this)
			updateDestroySignal(posDone, world, pos, start, Direction.EAST);
		
		if (world.getBlockState(pos.relative(Direction.WEST)).getBlock() == this)
			updateDestroySignal(posDone, world, pos, start, Direction.WEST);
		
		if (world.getBlockState(pos.relative(Direction.SOUTH)).getBlock() == this)
			updateDestroySignal(posDone, world, pos, start, Direction.SOUTH);
		
		if (world.getBlockState(pos.relative(Direction.NORTH)).getBlock() == this)
			updateDestroySignal(posDone, world, pos, start, Direction.NORTH);

		if (world.getBlockState(pos.relative(Direction.UP)).getBlock() == this)
			updateDestroySignal(posDone, world, pos, start, Direction.UP);
		
		if (world.getBlockState(pos.relative(Direction.DOWN)).getBlock() == this)
			updateDestroySignal(posDone, world, pos, start, Direction.DOWN);
	
		if (!pos.equals(start)) {
			world.setBlock(pos, state.setValue(POWERED, false), 8);
			world.getBlockTicks().scheduleTick(pos, this, 4);
		}
	}
	
	private void updateDestroySignal(List<BlockPos> posDone, World world, BlockPos pos, BlockPos start, Direction dir) {
		BlockPos posOffset = pos.relative(dir);
		BlockState state   = world.getBlockState(posOffset); 
		
		if (posDone.contains(posOffset)) {
			return;
		}
		
		posDone.add(posOffset);
		if (state.getBlock() != this)
			return;
		sendDestroySignal(posDone, world, state, posOffset, start);
	}
		
	@Override
	public VoxelShape getOcclusionShape(BlockState state, IBlockReader world, BlockPos pos) {
		if (state.getValue(POWERED)) return VoxelShapes.block();
		return VoxelShapes.empty();
	}	
}
