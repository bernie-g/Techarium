package software.bernie.techarium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.slaves.TopEnabledOnlySlave;

import javax.annotation.Nonnull;

public class BotariumTop extends MachineBlock<TopEnabledOnlySlave> {

    public BotariumTop() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5f).notSolid().noDrops(),TopEnabledOnlySlave::new);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0.0D, -16.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    }

}
