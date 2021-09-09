package software.bernie.techarium.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.client.ClientUtils;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.trait.behaviour.Behaviour;
import software.bernie.techarium.trait.behaviour.IHasBehaviour;
import software.bernie.techarium.trait.item.ItemBehaviour;
import software.bernie.techarium.trait.item.ItemTraits;

import java.util.List;

public class TechariumBlockItem<B extends TechariumBlock<?>> extends BlockItem implements IAnimatable, IHasBehaviour {
    protected final ItemBehaviour behaviour;
    public AnimationFactory factory = new AnimationFactory(this);
    public String controllerName = "controller";

    public TechariumBlockItem(B blockIn, Properties builder, ItemBehaviour behaviour) {
        super(blockIn, configure(builder, behaviour));
        this.behaviour = behaviour;

        behaviour.tweak(this);
    }

    public static Properties configure(Properties builder, ItemBehaviour behaviour) {
        behaviour.tweak(builder);
        return builder;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (worldIn == null)
            return;
        if (worldIn.isClientSide) {
            ((TechariumBlock<?>) getBlock()).getDescription().ifPresent(descriptionTrait -> {
                if (ClientUtils.isShift()) {
                    tooltip.add(descriptionTrait.description.get());
                } else {
                    tooltip.add(LangRegistry.machineShiftDescription.get());
                }
            });
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void registerControllers(AnimationData data) {
        behaviour.get(ItemTraits.GeckoLibItemRendering.class).ifPresent(trait -> {
            data.addAnimationController(new AnimationController(this, controllerName, 0, playState -> trait.getAnimationPredicate().apply(playState)));
        });
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
