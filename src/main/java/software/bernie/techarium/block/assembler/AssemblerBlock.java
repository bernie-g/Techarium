package software.bernie.techarium.block.assembler;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.assembler.AssemblerTile;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class AssemblerBlock extends MachineBlock<AssemblerTile>{

	public AssemblerBlock() {
		super(BlockBehaviours.assembler, AbstractBlock.Properties.of(Material.METAL));
	}
	
    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getBlockEntity(pos);
        if(tile instanceof AssemblerTile) {
            ((AssemblerTile) tile).setOpening(true);
        }
    }

}
