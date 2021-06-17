package software.bernie.techarium.trait.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ToolType;
import software.bernie.techarium.trait.Trait;

public class BlockTraits {
    public static abstract class MaterialTrait extends Trait {
        public MaterialTrait() {
            this.addTweaker(AbstractBlock.Properties.class, this::tweakProperties);
        }
        protected abstract void tweakProperties(AbstractBlock.Properties properties);
    }

    public static class MachineMaterialTrait extends MaterialTrait{
        @Override
        protected void tweakProperties(AbstractBlock.Properties properties) {
            properties.strength(4f).harvestLevel(2).harvestTool(ToolType.PICKAXE).noOcclusion().requiresCorrectToolForDrops();
        }
    }

    @AllArgsConstructor
    public static class TileEntityTrait<T extends TileEntity> extends Trait {
        private final Class<? extends T> tileClass;

        @SneakyThrows
        public T createTileEntity() {
            return tileClass.newInstance();
        }
    }

    @Data
    public static class ParticlesTrait extends Trait {
        private final boolean showBreakParticles;
    }

    @Data
    public static class BlockRenderTypeTrait extends Trait{
        private final BlockRenderType blockRenderType;
    }
}
