package software.bernie.techarium.trait.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.trait.behaviour.Behaviour;

import java.util.function.Function;

public class ItemBehaviour extends Behaviour {
    public static class Builder extends Behaviour.Builder<ItemBehaviour, ItemBehaviour.Builder> {
        public Builder() {
            super(new ItemBehaviour());
        }

        public Builder geoISTER(Function<AnimationEvent<? extends Item>, PlayState> animationPredicate, ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
            return this.with(new ItemTraits.GeckoLibItemRendering(animationPredicate, model, texture, animation));
        }

        public Builder geoISTER(Function<AnimationEvent<? extends Item>, PlayState> animationPredicate, AnimatedGeoModel<?> model) {
            return this.with(new ItemTraits.GeckoLibItemRendering(animationPredicate, model));
        }
    }

    public ItemBehaviour copy() {
        ItemBehaviour.Builder builder = new ItemBehaviour.Builder();
        for (Trait trait : this.traits.values()) {
            try {
                builder = builder.with((Trait) trait.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }
}
