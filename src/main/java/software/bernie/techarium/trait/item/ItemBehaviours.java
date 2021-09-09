package software.bernie.techarium.trait.item;


import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.techarium.util.Utils;

public class ItemBehaviours {
    public static ItemBehaviour base = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .build();

    public static ItemBehaviour botarium = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .with(new ItemTraits.GeckoLibItemRendering(event -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("Botarium.anim.idle", true));
                return PlayState.CONTINUE;
            },
                    Utils.rl("geo/botarium/botarium.geo.json"),
                    Utils.rl("textures/block/animated/botarium.png"),
                    Utils.rl("animations/botarium.animation.json")))
            .build();

    public static ItemBehaviour arboretum = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .with(new ItemTraits.GeckoLibItemRendering(event -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("Arboretum.anim.idle", true));
                return PlayState.CONTINUE;
            },
                    Utils.rl("geo/arboretum/arboretum.geo.json"),
                    Utils.rl("textures/block/animated/arboretum.png"),
                    Utils.rl("animations/arboretum.animation.json")))
            .build();

    public static ItemBehaviour gravMagnet = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .with(new ItemTraits.GeckoLibItemRendering(event -> PlayState.CONTINUE,
                    Utils.rl("geo/gravmagnet/gravmagnet.geo.json"),
                    Utils.rl("textures/block/animated/gravmagnet_push.png"),
                    Utils.rl("animations/gravmagnet.animation.json")))
            .build();

    public static ItemBehaviour magneticCoil = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .with(new ItemTraits.GeckoLibItemRendering(event -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
                return PlayState.CONTINUE;
            },
                    Utils.rl("geo/magneticcoil/magneticcoil.geo.json"),
                    Utils.rl("textures/block/animated/magneticcoil/magneticcoil_support.png"),
                    Utils.rl("animations/magneticcoil.animation.json")))
            .build();
}
