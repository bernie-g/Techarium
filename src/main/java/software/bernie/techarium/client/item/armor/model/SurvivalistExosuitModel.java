package software.bernie.techarium.client.item.armor.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.geckolib.StaticGeoModel;
import software.bernie.techarium.item.armor.SurvivalistExosuitItem;

public class SurvivalistExosuitModel extends StaticGeoModel<SurvivalistExosuitItem> {
    @Override
    public ResourceLocation getModelLocation(SurvivalistExosuitItem object) {
        return Techarium.rl("geo/armor/survivalist_exosuit.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SurvivalistExosuitItem object) {
        return Techarium.rl("textures/armor/survivalist_exosuit.png");
    }
}
