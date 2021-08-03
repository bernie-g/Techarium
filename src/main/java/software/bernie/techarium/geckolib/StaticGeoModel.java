package software.bernie.techarium.geckolib;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.techarium.Techarium;

public abstract class StaticGeoModel<T extends IAnimatable> extends AnimatedGeoModel<T> {
    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return Techarium.rl("animations/empty.animation.json");
    }
}
