package software.bernie.techarium.trait.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import software.bernie.techarium.trait.Trait;

@AllArgsConstructor
public class BlockRotationTrait extends Trait {

    @Getter RotationType rotationType;

    public BlockState getStateForPlacement(Block block, BlockItemUseContext context) {
        Direction dir;
        if (rotationType == RotationType.XZ) {
            dir = context.getHorizontalDirection().getOpposite();
        } else {
            dir = context.getPlayer().isShiftKeyDown() ? context.getNearestLookingDirection().getOpposite() : context.getNearestLookingDirection();
        }

        return block.defaultBlockState().setValue(rotationType.direction, dir);
    }

    public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(rotationType.direction);
    }

    public DirectionProperty getDirectionProperty() {
        return rotationType.direction;
    }

    public enum RotationType {
        XZ(BlockStateProperties.HORIZONTAL_FACING),
        XYZ(BlockStateProperties.FACING);

        DirectionProperty direction;

        RotationType(DirectionProperty dir) {
            direction = dir;
        }
    }
}