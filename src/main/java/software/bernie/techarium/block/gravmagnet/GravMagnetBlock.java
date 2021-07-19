package software.bernie.techarium.block.gravmagnet;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.techarium.block.base.MachineBlockRotationXYZ;
import software.bernie.techarium.tile.gravmagnet.GravMagnetTile;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class GravMagnetBlock extends MachineBlockRotationXYZ<GravMagnetTile> {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public GravMagnetBlock() {
		super(BlockBehaviours.gravMagnet, AbstractBlock.Properties.of(Material.METAL));
		this.registerDefaultState(this.defaultBlockState().setValue(POWERED, Boolean.valueOf(false)));
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).setValue(POWERED,
				Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos())));
	}

	public void neighborChanged(BlockState state, World world, BlockPos pos1, Block block, BlockPos pos2, boolean f) {
		if (!world.isClientSide()) {
			boolean flag = world.hasNeighborSignal(pos1);
			if (flag != state.getValue(POWERED)) {
				world.setBlock(pos1, state.setValue(POWERED, flag), 2);
			}
		}
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
		super.createBlockStateDefinition(container);
		container.add(POWERED);
	}

}
