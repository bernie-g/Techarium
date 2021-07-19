package software.bernie.techarium.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.trait.Traits;

public class BlockRotationTrait extends Trait {
    
	protected RotationType rotationType;
	
    public BlockState getStateForPlacement(BlockItemUseContext context) {
    	Direction dir;
    	if (rotationType == RotationType.XZ)
    		dir = context.getHorizontalDirection().getOpposite();
    	else
    	   	dir = context.getPlayer().isShiftKeyDown() ? context.getNearestLookingDirection().getOpposite() : context.getNearestLookingDirection();
        
    	return defaultBlockState().setValue(rotationType, dir);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(rotationType.direction);
    }
	
	public static enum RotationType {
		XZ(BlockStateProperties.HORIZONTAL_FACING),
		XYZ(BlockStateProperties.FACING);
		
		DirectionProperty direction;
		
		private RotationType(DirectionProperty dir) {
			direction = dir;
		}
	}
}

