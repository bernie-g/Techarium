package software.bernie.techarium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BotariumTop extends Block
{
	public BotariumTop()
	{
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5f).notSolid().noDrops());
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid)
	{
		BlockState blockState = world.getBlockState(pos.down());
		if(blockState.getBlock() instanceof BotaniumMaster)
		{
			world.destroyBlock(pos.down(), !player.isCreative());
		}
		super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		return true;
	}



	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
}
