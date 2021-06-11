package software.bernie.techarium.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.base.MachineTileBase;

import java.util.List;

public class MachineItem<B extends MachineBlock<T>, T extends MachineTileBase> extends BlockItem {

    public MachineItem(B blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    protected boolean canPlace(BlockItemUseContext context, BlockState state) {
        if (super.canPlace(context, state)) {
            if (((MachineBlock) getBlock()).canBePlaced(context.getLevel(), context.getClickedPos())) {
                return true;
            } else if (context.getLevel().isClientSide()) {
                //TODO: Show bounding box or something similar, if the call comes from a client I'm too dumb for this rendering thing
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
