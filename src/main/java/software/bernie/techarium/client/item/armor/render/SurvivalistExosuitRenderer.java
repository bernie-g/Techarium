package software.bernie.techarium.client.item.armor.render;

import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.techarium.client.item.armor.model.SurvivalistExosuitModel;
import software.bernie.techarium.item.armor.SurvivalistExosuitItem;

public class SurvivalistExosuitRenderer extends GeoArmorRenderer<SurvivalistExosuitItem> {
    public SurvivalistExosuitRenderer() {
        super(new SurvivalistExosuitModel());

        //ycar is braindead
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftBootBone = "armorRightBoot";
    }
}
