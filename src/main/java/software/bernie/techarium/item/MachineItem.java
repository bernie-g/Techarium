package software.bernie.techarium.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import software.bernie.techarium.block.base.MachineBlock;

import net.minecraft.item.Item.Properties;

public class MachineItem extends BlockItem {


    public MachineItem(MachineBlock<?> blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    protected boolean canPlace(BlockItemUseContext context, BlockState state) {
        if (super.canPlace(context, state)) {
            if (((MachineBlock)getBlock()).canBePlaced(context.getLevel(), context.getClickedPos())) {
                return true;
            } else if (context.getLevel().isClientSide()){
                //TODO: Show bounding box or something similar, if the call comes from a client I'm too dumb for this rendering thing
            }
        }
        return false;
    }
}
