package software.bernie.techarium.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;

public class ItemBotarium extends BlockItem
{
	public ItemBotarium(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}

	@Override
	protected boolean canPlace(BlockItemUseContext context, BlockState state)
	{
		return super.canPlace(context, state) && context.getWorld().getBlockState(context.getPos().up()).isAir();
	}


}
