package software.bernie.techarium.block.depot;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.depot.DepotTileEntity;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class DepotBlock extends MachineBlock<DepotTileEntity> {
    public DepotBlock() {
        super(BlockBehaviours.DEPOT, AbstractBlock.Properties.copy(Blocks.IRON_BLOCK));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return box(0,0,0,16,13,16);
    }
}
