package software.bernie.techarium.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class ExchangeStationBlock extends MachineBlock<ExchangeStationTile> {

    public ExchangeStationBlock() {
        super(BlockBehaviours.exchangeStation, AbstractBlock.Properties.of(Material.METAL));
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getBlockEntity(pos);
        if(tile instanceof ExchangeStationTile) {
            ((ExchangeStationTile) tile).setOpening(true);
        }
    }
}
