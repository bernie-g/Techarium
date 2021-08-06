package software.bernie.techarium.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.client.ClientUtils;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.tile.base.MachineTileBase;

import java.util.List;

public class TechariumBlockItem<B extends TechariumBlock<T>, T extends MachineTileBase> extends BlockItem {

    public TechariumBlockItem(B blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (worldIn == null)
            return;
        if (worldIn.isClientSide) {
            ((TechariumBlock<?>)getBlock()).getDescription().ifPresent(descriptionTrait -> {
                if (ClientUtils.isShift()) {
                    tooltip.add(descriptionTrait.description.get());
                } else {
                    tooltip.add(LangRegistry.machineShiftDescription.get());
                }
            });
        }
    }
}
