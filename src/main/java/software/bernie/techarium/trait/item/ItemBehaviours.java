package software.bernie.techarium.trait.item;


import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.client.tile.model.ArboretumModel;
import software.bernie.techarium.client.tile.model.BotariumModel;
import software.bernie.techarium.client.tile.model.GravMagnetModel;
import software.bernie.techarium.client.tile.model.MagneticCoilModel;
import software.bernie.techarium.tile.gravmagnet.GravMagnetTile;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;
import software.bernie.techarium.util.Utils;

public class ItemBehaviours {
    public static ItemBehaviour base = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .build();

    public static ItemBehaviour botarium = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .geoISTER(event -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("Botarium.anim.idle", true));
                return PlayState.CONTINUE;
            }, new BotariumModel())
            .build();

    public static ItemBehaviour arboretum = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .geoISTER(event -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("Arboretum.anim.idle", true));
                return PlayState.CONTINUE;
            }, new ArboretumModel())
            .build();

    public static ItemBehaviour gravMagnet = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .geoISTER(event -> PlayState.CONTINUE, new GravMagnetModel() {
                public ResourceLocation getTextureLocation(GravMagnetTile tile) {
                    return new ResourceLocation(Techarium.MOD_ID, "textures/block/animated/gravmagnet_push.png");
                }
            })
            .build();

    public static ItemBehaviour magneticCoil = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.partialBaseItem)
            .geoISTER(event -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
                return PlayState.CONTINUE;
            }, new MagneticCoilModel() {
                public ResourceLocation getTextureLocation(MagneticCoilTile tile) {
                    return new ResourceLocation(Techarium.MOD_ID, "textures/block/animated/magneticcoil/magneticcoil_support.png");
                }
            })
            .build();
}
