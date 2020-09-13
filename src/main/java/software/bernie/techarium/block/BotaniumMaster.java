package software.bernie.techarium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.BotariumTile;

public class BotaniumMaster extends MachineBlock<BotariumTile> {

    public BotaniumMaster() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5f).harvestLevel(2).harvestTool(ToolType.PICKAXE).notSolid(), BotariumTile::new);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof BotariumTile)
        {
            ((BotariumTile) tile).isOpening = true;
        }
    }
}
