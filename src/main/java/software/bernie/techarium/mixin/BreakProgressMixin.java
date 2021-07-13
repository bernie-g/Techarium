package software.bernie.techarium.mixin;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.techarium.tile.slaves.SlaveTile;

@Mixin(ClientWorld.class)
public class BreakProgressMixin {
    @Shadow
    @Final
    private WorldRenderer levelRenderer;

    private final ClientWorld self = (ClientWorld) (Object) this;

    @Inject(at = @At("HEAD"), method = "destroyBlockProgress")
    private void onBreakProgress(int playerEntityId, BlockPos pos, int progress, CallbackInfo ci) {
        TileEntity blockEntity = self.getBlockEntity(pos);
        if(blockEntity instanceof SlaveTile){
           ((SlaveTile) blockEntity).setBlockBreakProgress(self, this.levelRenderer, playerEntityId, pos, progress);
        }
    }
}