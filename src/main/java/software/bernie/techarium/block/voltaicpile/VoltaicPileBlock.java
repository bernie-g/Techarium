package software.bernie.techarium.block.voltaicpile;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.tile.voltaicpile.VoltaicPileTile;
import software.bernie.techarium.trait.block.BlockBehaviours;
import software.bernie.techarium.util.TechariumMaterial;

public class VoltaicPileBlock extends TechariumBlock<VoltaicPileTile> implements IAnimatable {
    public VoltaicPileBlock() {
        super(BlockBehaviours.voltaicPile, AbstractBlock.Properties.of(TechariumMaterial.METAL));
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (world.getBlockEntity(pos) instanceof VoltaicPileTile) {
            int stored = ((VoltaicPileTile) world.getBlockEntity(pos)).getEnergyStorage().getEnergyStored();
            if (stored > 0) {
                ItemStack itemStack = new ItemStack(this);
                CompoundNBT compoundNBT = world.getBlockEntity(pos).save(new CompoundNBT());
                compoundNBT.remove("x");
                compoundNBT.remove("y");
                compoundNBT.remove("z");
                itemStack.addTagElement("BlockEntityTag", compoundNBT);
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
            }
            else {
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemRegistry.COPPER_INGOT.get()));
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemRegistry.ZINC_INGOT.get()));
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
}