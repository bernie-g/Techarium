package software.bernie.techarium.block.voltaicpile;

import net.minecraft.block.*;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.IStringSerializable;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.tile.voltaicpile.VoltaicPileTile;
import software.bernie.techarium.trait.block.BlockBehaviours;
import software.bernie.techarium.util.TechariumMaterial;

import java.util.Locale;

public class VoltaicPileBlock extends TechariumBlock<VoltaicPileTile> {
    public static final EnumProperty<Charge> CHARGE = EnumProperty.create("charge", Charge.class);

    public VoltaicPileBlock() {
        super(BlockBehaviours.voltaicPile, AbstractBlock.Properties.of(TechariumMaterial.METAL));
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public enum Charge implements IStringSerializable {
        EMPTY, ONE_THIRD, TWO_THIRD, FULL;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
