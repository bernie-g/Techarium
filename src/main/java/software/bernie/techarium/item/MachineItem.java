package software.bernie.techarium.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.tile.base.MachineTileBase;
import software.bernie.techarium.client.ClientUtils;
import software.bernie.techarium.trait.item.ItemBehaviour;
import software.bernie.techarium.trait.item.ItemTraits;

import java.util.List;
import java.util.function.Function;

public class MachineItem<B extends MachineBlock<T>, T extends MachineTileBase> extends TechariumBlockItem<B> {
    public MachineItem(B blockIn, Properties builder, ItemBehaviour behaviour) {
        super(blockIn, builder, behaviour);
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
}
