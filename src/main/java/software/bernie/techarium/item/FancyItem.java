package software.bernie.techarium.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.base.MachineTileBase;

import java.util.function.Function;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUM;

public class FancyItem<B extends MachineBlock<T>, T extends MachineTileBase> extends MachineItem<B, T> implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);
    public String controllerName = "controller";
    private final Function<AnimationEvent<? extends Item>, PlayState> animationPredicate;

    public FancyItem(B b, Function<AnimationEvent<? extends Item>, PlayState> animationPredicate, ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        super(b, new Item.Properties().tab(TECHARIUM).setISTER(() -> () -> new ItemRender(model, texture, animation)));
        this.animationPredicate = animationPredicate;
    }

    public FancyItem(B b, Function<AnimationEvent<? extends Item>, PlayState> animationPredicate, String model, String texture, String animation) {
        super(b, new Item.Properties().tab(TECHARIUM).setISTER(() -> () -> new ItemRender(model, texture, animation)));
        this.animationPredicate = animationPredicate;
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return animationPredicate.apply(event);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, controllerName, 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
