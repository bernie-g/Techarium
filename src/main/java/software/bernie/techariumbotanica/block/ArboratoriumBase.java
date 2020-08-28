package software.bernie.techariumbotanica.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import software.bernie.techariumbotanica.registry.BlockRegistry;
import software.bernie.techariumbotanica.tile.TileArboratorium;
import software.bernie.techariumbotanica.tile.TileBotarium;

import javax.annotation.Nullable;

public class ArboratoriumBase extends Block
{
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public ArboratoriumBase()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(3.5f).harvestLevel(2).harvestTool(ToolType.PICKAXE).notSolid());
		this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
	}


	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos.up(), BlockRegistry.BOTARIUM_TOP.get().getDefaultState());
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}


	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileArboratorium();
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid)
	{
		BlockState blockState = world.getBlockState(pos.up());
		if(blockState.getBlock() instanceof BotariumTop)
		{
			world.destroyBlock(pos.up(), !player.isCreative());
		}
		super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		return true;
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return Block.makeCuboidShape(0, 0, 0, 16, 32, 16);
	}
}
