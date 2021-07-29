package software.bernie.techarium.block.depot;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.depot.DepotTileEntity;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class DepotBlock extends MachineBlock<DepotTileEntity> {
    public DepotBlock() {
        super(BlockBehaviours.DEPOT, AbstractBlock.Properties.copy(Blocks.IRON_BLOCK));
    }
}
