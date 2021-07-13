package software.bernie.techarium.block.slave;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.tile.slaves.SlaveTile;
import software.bernie.techarium.trait.block.BlockBehaviour;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class SlaveBlock extends MachineBlock<SlaveTile> {
    protected final Lazy<TechariumBlock> masterBlock;

    public SlaveBlock(RegistryObject<TechariumBlock> masterBlock, BlockBehaviour behaviour) {
        super(BlockBehaviours.createSlave(behaviour), Block.Properties.of(Material.METAL));
        this.masterBlock = Lazy.of(() -> masterBlock.get());
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

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return masterBlock.get().getPickBlock(state, target, world, pos, player);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        world.destroyBlockProgress(pos.hashCode(), pos, -1);
        TileEntity blockEntity = world.getBlockEntity(pos);
        if(!world.isClientSide() && state.getBlock() != newState.getBlock() && !isMoving && blockEntity instanceof SlaveTile){
            Vector3i offset = ((SlaveTile) blockEntity).getMasterOffset().get();
            BlockPos masterPos = pos.offset(offset);
            world.destroyBlockProgress(masterPos.hashCode(), masterPos, -1);
        }

        super.onRemove(state, world, pos, newState, isMoving);
    }
}
