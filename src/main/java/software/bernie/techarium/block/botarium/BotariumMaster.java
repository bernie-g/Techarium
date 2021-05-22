package software.bernie.techarium.block.botarium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.botarium.BotariumTile;
import software.bernie.techarium.util.BlockRegion;

import javax.annotation.Nonnull;

public class BotariumMaster extends MachineBlock<BotariumTile> {

    public BotariumMaster() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5f).harvestLevel(2).harvestTool(ToolType.PICKAXE).notSolid().setRequiresTool(), BotariumTile::new);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof BotariumTile) {
            ((BotariumTile) tile).setOpening(true);
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof BotariumTile) {
            BotariumTile tile = (BotariumTile) world.getTileEntity(pos);
            return Math.max(tile.getFluidInventory().getFluid().getFluid().getDefaultState().getBlockState().getLightValue(world, pos), super.getLightValue(state, world, pos));
        }
        return super.getLightValue(state, world, pos);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D);
    }

    @Override
    public BlockRegion getBlockSize() {
        return new BlockRegion(1,2,1);
    }
}
