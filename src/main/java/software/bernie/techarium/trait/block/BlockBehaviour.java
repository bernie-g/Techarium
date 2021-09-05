package software.bernie.techarium.trait.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import software.bernie.techarium.registry.lang.LangEntry;
import software.bernie.techarium.trait.Traits;
import software.bernie.techarium.trait.behaviour.Behaviour;
import software.bernie.techarium.trait.block.BlockTraits.BlockRotationTrait;

import java.util.Map;

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

        public Builder description(LangEntry description) {
            return this.with(new Traits.DescriptionTrait(description));
        }

        public Builder rotation(BlockTraits.RotationType rotation) {
            return this.with(new BlockRotationTrait(rotation));
        }

        public Builder property(Map<Property<?>, Object> propertyMap) {
            return this.with(new BlockTraits.PropertyTrait(propertyMap));
        }
    }
}
