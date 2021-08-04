package software.bernie.techarium.block.gravmagnet;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.gravmagnet.GravMagnetTile;
import software.bernie.techarium.trait.block.BlockBehaviours;

import javax.annotation.Nullable;

public class GravMagnetBlock extends MachineBlock<GravMagnetTile> {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public GravMagnetBlock() {
		super(BlockBehaviours.gravMagnet, AbstractBlock.Properties.of(Material.METAL));
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext useContext) {
		return super.getStateForPlacement(useContext).setValue(POWERED,
				Boolean.valueOf(useContext.getLevel().hasNeighborSignal(useContext.getClickedPos())));
	}

	public void neighborChanged(BlockState state, World world, BlockPos pos1, Block block, BlockPos pos2, boolean f) {
		if (!world.isClientSide()) {
			boolean flag = world.hasNeighborSignal(pos1);
			if (flag != state.getValue(POWERED)) {
				world.setBlock(pos1, state.setValue(POWERED, flag), 2);
			}
		}
	}
	
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		return ActionResultType.PASS;
	}
}
