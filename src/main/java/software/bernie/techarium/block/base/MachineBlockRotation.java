package software.bernie.techarium.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import software.bernie.techarium.tile.base.MachineTileBase;
import software.bernie.techarium.trait.block.BlockBehaviour;
import software.bernie.techarium.trait.block.BlockTraits;

public class MachineBlockRotation<T extends MachineTileBase> extends MachineBlock<T> {
	
    public MachineBlockRotation(BlockBehaviour behaviour, Properties properties) {
		super(behaviour, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(getBehaviour().get(BlockTraits.BlockRotationTrait.class).get().getDirectionProperty(), Direction.NORTH));
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
    	return getBehaviour().get(BlockTraits.BlockRotationTrait.class).get().getStateForPlacement(this, context);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    	builder.add(getDirectionProperty());
   }
    
    public DirectionProperty getDirectionProperty() {
    	return getBehaviour().get(BlockTraits.BlockRotationTrait.class).get().getDirectionProperty();
    }
    
}
