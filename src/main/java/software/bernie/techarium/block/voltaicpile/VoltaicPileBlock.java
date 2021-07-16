package software.bernie.techarium.block.voltaicpile;

import net.minecraft.block.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.tile.voltaicpile.VoltaicPileTile;
import software.bernie.techarium.trait.block.BlockBehaviours;
import software.bernie.techarium.util.TechariumMaterial;

public class VoltaicPileBlock extends TechariumBlock<VoltaicPileTile> {
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Charge> CHARGE = EnumProperty.create("charge", Charge.class);

    public VoltaicPileBlock() {
        super(BlockBehaviours.voltaicPile, AbstractBlock.Properties.of(TechariumMaterial.METAL));

        this.registerDefaultState(this.getStateDefinition().any().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(CHARGE, Charge.FULL));
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, CHARGE);
    }
}
