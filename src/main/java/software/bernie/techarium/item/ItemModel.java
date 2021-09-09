package software.bernie.techarium.item;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemModel extends AnimatedGeoModel<TechariumBlockItem> {
	private ResourceLocation model;
	private ResourceLocation texture;
	private ResourceLocation animation;

	public ItemModel(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
		this.model = model;
		this.texture = texture;
		this.animation = animation;
	}

	public ItemModel(String model, String texture, String animation) {
		this.model = new ResourceLocation(model);
		this.texture = new ResourceLocation(texture);
		this.animation = new ResourceLocation(animation);
	}

	@Override
	public ResourceLocation getModelLocation(TechariumBlockItem object) {
		return model;
	}

	@Override
	public ResourceLocation getTextureLocation(TechariumBlockItem object) {
		return texture;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(TechariumBlockItem animatable) {
		return animation;
	}
}
