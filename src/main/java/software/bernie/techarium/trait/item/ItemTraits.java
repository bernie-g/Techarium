package software.bernie.techarium.trait.item;

import lombok.Data;
import lombok.SneakyThrows;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.techarium.item.ItemRender;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.util.TechariumEnergyStorage;

import java.util.function.Function;

public class ItemTraits {
    @Data
    public static class GeckoLibItemRendering extends Trait {
        private Function<AnimationEvent<? extends Item>, PlayState> animationPredicate;
        private ResourceLocation model, texture, animation;

        public GeckoLibItemRendering(Function<AnimationEvent<? extends Item>, PlayState> animationPredicate, ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
            this.animationPredicate = animationPredicate;
            this.model = model;
            this.texture = texture;
            this.animation = animation;

            addTweaker(Item.Properties.class, this::addISTER);
        }

        private void addISTER(Item.Properties properties) {
            properties.setISTER(() -> () -> new ItemRender(model, texture, animation));
        }
    }
}
