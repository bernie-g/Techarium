package software.bernie.techarium.block.slave;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.slaves.SlaveTile;
import software.bernie.techarium.trait.block.BlockBehaviour;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class SlaveBlock extends MachineBlock<SlaveTile> {
    public SlaveBlock(BlockBehaviour behaviour) {
        super(BlockBehaviours.createSlave(BlockBehaviours.botarium), Block.Properties.of(Material.METAL));
    }

    @Override
    public float getDestroyProgress(BlockState p_180647_1_, PlayerEntity p_180647_2_, IBlockReader p_180647_3_, BlockPos p_180647_4_) {
        return super.getDestroyProgress(p_180647_1_, p_180647_2_, p_180647_3_, p_180647_4_);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader world, BlockPos pos, ISelectionContext p_220053_4_) {
        TileEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof SlaveTile){
            VoxelShape boundingBox = ((SlaveTile) blockEntity).getBoundingBox();
            return boundingBox == null ? Block.box(0, 0, 0, 16, 16, 16) : boundingBox;
        }
        return Block.box(0, 0, 0, 16, 16, 16);

    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext p_220071_4_) {
        TileEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SlaveTile) {
            VoxelShape boundingBox = ((SlaveTile) blockEntity).getCollisionBox();
            return boundingBox == null ? Block.box(0, 0, 0, 16, 16, 16) : boundingBox;
        }
        return Block.box(0, 0, 0, 16, 16, 16);

    }
}
