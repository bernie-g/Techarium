package software.bernie.techarium.trait.block;

import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.tile.base.MachineSlaveTile;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.trait.behaviour.Behaviour;

@Data
public class SlaveBlockTrait extends Trait {
    private final BlockBehaviour masterTrait;

    @Override
    public void verifyTrait(Behaviour behaviour) throws IllegalStateException {
        if (!MachineSlaveTile.class
                .isAssignableFrom(behaviour.getRequired(BlockTraits.TileEntityTrait.class).tileClass)) {
            throw new IllegalStateException("Slave block must have slave tile attached!");
        }
    }

    public void handleDestruction(World world, BlockPos pos, boolean shouldHarvest) {
        TileEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MachineSlaveTile) {
            MachineSlaveTile slaveEntity = (MachineSlaveTile) blockEntity;
            TileEntity masterTile = slaveEntity.getMasterTile();
            if (masterTile != null && !slaveEntity.isBeingDestroyed) {
                Block block = masterTile.getBlockState().getBlock();
                if (block instanceof TechariumBlock) {
                    ((TechariumBlock) block).getBehaviour().get(MasterBlockTrait.class)
                            .ifPresent(trait -> trait
                                    .handleDestruction(world, pos.offset(slaveEntity.masterOffset.get()), masterTile
                                            .getBlockState(), shouldHarvest));
                }
            }
        }
    }

    public void placeSlaves(World world, BlockPos pos) {
        TileEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MachineSlaveTile) {
            TileEntity masterTile = ((MachineSlaveTile) blockEntity).getMasterTile();
            Block block = masterTile.getBlockState().getBlock();
            if (block instanceof TechariumBlock) {
                ((TechariumBlock) block).getBehaviour().get(MasterBlockTrait.class)
                        .ifPresent(trait -> trait.placeSlaves(world, pos));
            }
        }
    }

    public void drop(World world, BlockPos pos, PlayerEntity player) {
        TileEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MachineSlaveTile) {
            TileEntity masterTile = ((MachineSlaveTile) blockEntity).getMasterTile();
            BlockState blockState = masterTile.getBlockState();
            Block block = blockState.getBlock();
            if (block instanceof TechariumBlock) {
                TileEntity tileentity = blockState.hasTileEntity() ? world.getBlockEntity(pos) : null;
                Block.dropResources(blockState, world, pos, tileentity, player, ItemStack.EMPTY);
            }
        }
    }
}
