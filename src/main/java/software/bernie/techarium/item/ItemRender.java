package software.bernie.techarium.item;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemRender extends GeoItemRenderer<FancyItem> {
	public ItemRender(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
		super(new ItemModel(model, texture, animation));
	}
	public ItemRender(String model, String texture, String animation) {
		super(new ItemModel(model, texture, animation));
	}
}