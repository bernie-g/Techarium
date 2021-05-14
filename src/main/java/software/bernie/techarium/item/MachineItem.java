package software.bernie.techarium.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import software.bernie.techarium.block.base.MachineBlock;

public class MachineItem extends BlockItem {


    public MachineItem(MachineBlock<?> blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    protected boolean canPlace(BlockItemUseContext context, BlockState state) {
        if (super.canPlace(context, state)) {
            if (((MachineBlock)getBlock()).canBePlaced(context.getWorld(), context.getPos())) {
                return true;
            } else if (context.getWorld().isRemote()){
                //TODO: Show bounding box or something similar, if the call comes from a client I'm too dumb for this rendering thing
            }
        }
        return false;
    }
}
