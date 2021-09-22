package software.bernie.techarium.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemRender extends GeoItemRenderer<TechariumBlockItem> {
	public ItemRender(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
		super(new ItemModel(model, texture, animation));
	}

	@Override
	public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack stack, IRenderTypeBuffer bufferIn, int combinedLightIn, int overlayIn) {
		stack.pushPose();
		if (transformType == ItemCameraTransforms.TransformType.FIXED) {
			stack.mulPose(Vector3f.ZP.rotationDegrees(90));
			stack.translate(0.14,- 1.05d,-0.14);
		}
		super.renderByItem(itemStack, transformType, stack, bufferIn, combinedLightIn, overlayIn);
		stack.popPose();
	}
}