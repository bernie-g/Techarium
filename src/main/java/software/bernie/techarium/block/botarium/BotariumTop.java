package software.bernie.techarium.block.botarium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.registry.BlockTileRegistry;
import software.bernie.techarium.tile.slaves.TopEnabledOnlySlave;

import javax.annotation.Nonnull;

public class BotariumTop extends MachineBlock<TopEnabledOnlySlave> {

    public BotariumTop() {
        super(Block.Properties.of(Material.METAL).strength(3.5f).harvestLevel(2).harvestTool(ToolType.PICKAXE).noOcclusion().requiresCorrectToolForDrops().noDrops(),TopEnabledOnlySlave::new);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.box(0.0D, -16.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    }

    @Override
    public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(BlockTileRegistry.BOTARIUM.getItem());
    }
}
