package software.bernie.techarium.client.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.techarium.client.tile.model.DepotModel;
import software.bernie.techarium.machine.addon.inventory.InventoryAddon;
import software.bernie.techarium.tile.depot.DepotTileEntity;

public class DepotRenderer extends GeoBlockRenderer<DepotTileEntity> {
    public DepotRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new DepotModel());
    }

    @Override
    public void renderLate(DepotTileEntity depot, MatrixStack matrixStack, float partialTicks, IRenderTypeBuffer buffer, IVertexBuilder bufferIn, int combinedLight, int combinedOverlay, float red, float green, float blue, float alpha) {
        InventoryAddon input = depot.getController().getMultiInventory().getInventoryByName("input").get();
        InventoryAddon output = depot.getController().getMultiInventory().getInventoryByName("output").get();
        for (int i = 0; i < DepotTileEntity.SLOTS; i++) {
            ItemStack toRender = input.getStackInSlot(i);
            if (toRender.isEmpty())
                toRender = output.getStackInSlot(i);
            if (toRender.isEmpty())
                continue;
            matrixStack.pushPose();
            changeMatrixStackForSlot(matrixStack, getGeoModelProvider().getBone("item" + (i+1)));
            Minecraft.getInstance().getItemRenderer().renderStatic(toRender, ItemCameraTransforms.TransformType.NONE, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.popPose();
        }
    }

    private void changeMatrixStackForSlot(MatrixStack matrixStack, IBone bone) {
        matrixStack.translate(bone.getPivotX()/16f, bone.getPivotY()/16f, bone.getPivotZ()/16f);
        matrixStack.translate(bone.getPositionX()/16f, bone.getPositionY()/16f, bone.getPositionZ()/16f);
        matrixStack.mulPose(new Quaternion(bone.getRotationX(), bone.getRotationY(), bone.getRotationZ(), false));
        matrixStack.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
    }
}
