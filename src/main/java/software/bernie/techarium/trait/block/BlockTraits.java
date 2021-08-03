package software.bernie.techarium.trait.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ToolType;
import software.bernie.techarium.trait.Trait;

@UtilityClass
public class BlockTraits {
    public abstract static class MaterialTrait extends Trait {
        protected MaterialTrait() {
            this.addTweaker(AbstractBlock.Properties.class, this::tweakProperties);
        }
        protected abstract void tweakProperties(AbstractBlock.Properties properties);
    }

    public static class MachineMaterialTrait extends MaterialTrait{
        @Override
        protected void tweakProperties(AbstractBlock.Properties properties) {
            properties.strength(4f).harvestLevel(2).harvestTool(ToolType.PICKAXE).noOcclusion().requiresCorrectToolForDrops();
        }
    }

    @AllArgsConstructor
    public static class TileEntityTrait<T extends TileEntity> extends Trait {
        private final Class<? extends T> tileClass;

        @SneakyThrows
        public T createTileEntity() {
            return tileClass.getDeclaredConstructor().newInstance();
        }
    }

    @Data
    public static class ParticlesTrait extends Trait {
        private final boolean showBreakParticles;
    }

    @Data
    public static class BlockRenderTypeTrait extends Trait {
        private final BlockRenderType blockRenderType;
    }
    
    public static class BlockRotationTrait extends Trait {

    	protected final RotationType rotationType;

    	public BlockRotationTrait(RotationType rotationType) {
    		this.rotationType = rotationType;
    	}

        public BlockState getStateForPlacement(Block block, BlockItemUseContext context) {
        	Direction dir;
        	if (rotationType == RotationType.XZ)
        		dir = context.getHorizontalDirection().getOpposite();
        	else
        	   	dir = context.getPlayer().isShiftKeyDown() ? context.getNearestLookingDirection().getOpposite() : context.getNearestLookingDirection();

        	return block.defaultBlockState().setValue(rotationType.direction, dir);
        }

        public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
            builder.add(rotationType.direction);
        }

        public DirectionProperty getDirectionProperty() {
        	return rotationType.direction;
        }
    }

	public enum RotationType {
		XZ(BlockStateProperties.HORIZONTAL_FACING),
		XYZ(BlockStateProperties.FACING);

		DirectionProperty direction;

		private RotationType(DirectionProperty dir) {
			direction = dir;
		}
	}
}
