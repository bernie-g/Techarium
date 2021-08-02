package software.bernie.techarium.trait.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.shapes.VoxelShape;
import software.bernie.techarium.registry.lang.LangEntry;
import software.bernie.techarium.trait.Traits;
import software.bernie.techarium.trait.behaviour.Behaviour;
import software.bernie.techarium.trait.block.BlockTraits.BlockRotationTrait;

public class BlockBehaviour extends Behaviour {
    public static class Builder extends Behaviour.Builder<BlockBehaviour, BlockBehaviour.Builder> {
        public Builder() {
            super(new BlockBehaviour());
        }

        public Builder showBreakParticles(boolean showBreakParticles) {
            return this.with(new BlockTraits.ParticlesTrait(showBreakParticles));
        }

        public Builder staticModel() {
            return this.with(new BlockTraits.BlockRenderTypeTrait(BlockRenderType.MODEL));
        }

        public Builder animatedModel() {
            return this.with(new BlockTraits.BlockRenderTypeTrait(BlockRenderType.ENTITYBLOCK_ANIMATED));
        }

        public Builder tileEntity(Class<? extends TileEntity> tileEntityType){
            return this.with(new BlockTraits.TileEntityTrait<>(tileEntityType));
        }

        public Builder shape(VoxelShape shape){
            return this.with(new BlockTraits.VoxelShapeTrait(shape, shape));
        }

        public Builder shape(VoxelShape boundingBoxShape, VoxelShape collisionBoxShape){
            return this.with(new BlockTraits.VoxelShapeTrait(boundingBoxShape, collisionBoxShape));
        }

        public Builder description(LangEntry description) {
            return this.with(new Traits.DescriptionTrait(description));
        }

        public Builder rotation(BlockTraits.RotationType rotation) {
            return this.with(new BlockRotationTrait(rotation));
        }
    }
}
