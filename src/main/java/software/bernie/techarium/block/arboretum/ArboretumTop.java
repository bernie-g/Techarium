package software.bernie.techarium.block.arboretum;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import software.bernie.techarium.block.slave.SlaveBlock;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.trait.block.BlockBehaviours;

import javax.annotation.Nonnull;

public class ArboretumTop extends SlaveBlock {

    public ArboretumTop() {
        super(BlockBehaviours.arboretum);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return Block.box(0.0D, -16.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    }

    @Override
    public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(BlockRegistry.ARBORETUM.getItem());
    }
}
