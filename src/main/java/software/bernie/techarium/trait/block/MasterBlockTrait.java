package software.bernie.techarium.trait.block;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.tile.base.MachineSlaveTile;
import software.bernie.techarium.tile.base.TechariumTileEntity;
import software.bernie.techarium.tile.slaves.SlaveTile;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.trait.behaviour.Behaviour;
import software.bernie.techarium.util.ShapeUtils;
import software.bernie.techarium.util.SlaveSpot;
import software.bernie.techarium.util.VectorUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class MasterBlockTrait extends Trait {
    public final String name;
    private List<SlaveSpot> slaveSpots = new ArrayList<>();
    private List<SlaveSpot> slaveCollisions = new ArrayList<>();

    @Override
    public void verifyTrait(Behaviour behaviour) throws IllegalStateException {
        BlockTraits.VoxelShapeTrait voxelShape = behaviour.getRequired(BlockTraits.VoxelShapeTrait.class);
        updateSlaveSpots(voxelShape.getBoundingBox(), voxelShape.getCollisionBox());

        if (!TechariumTileEntity.class
                .isAssignableFrom(behaviour.getRequired(BlockTraits.TileEntityTrait.class).tileClass)) {
            throw new IllegalStateException("Slave block must have slave tile attached!");
        }
    }

    public void updateSlaveSpots(VoxelShape boundingBox, VoxelShape collisionBox) {
        this.slaveSpots = ShapeUtils.getSlaveOffsets(boundingBox);
        this.slaveCollisions = ShapeUtils.getSlaveOffsets(collisionBox);
    }

    public void handleDestruction(World world, BlockPos pos, BlockState state, boolean shouldHarvest) {
        TileEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MachineMasterTile) {
            MachineMasterTile<?> masterTile = (MachineMasterTile<?>) blockEntity;
            masterTile.masterHandleDestruction();
            for (SlaveSpot slaveSpot : slaveSpots) {
                Vector3i rotatedOffset = slaveSpot.getOffset();
                if (state.hasProperty(HorizontalBlock.FACING)) {
                    rotatedOffset = VectorUtils
                            .rotateVector2D(rotatedOffset, state.getValue(HorizontalBlock.FACING).getOpposite());
                }

                BlockPos slavePos = pos.offset(rotatedOffset);
                TileEntity slaveTile = world.getBlockEntity(slavePos);
                if (slaveTile instanceof MachineSlaveTile) {
                    ((MachineSlaveTile) slaveTile).isBeingDestroyed = true;
                    world.destroyBlock(slavePos, false);
                }
            }
            world.destroyBlock(pos, shouldHarvest);
        }
    }

    public void placeSlaves(World world, BlockPos pos) {
        if (world != null) {
            BlockState state = world.getBlockState(pos);

            for (SlaveSpot slaveSpot : this.slaveSpots) {
                Vector3i rotatedOffset = slaveSpot.getOffset();
                if (state.hasProperty(HorizontalBlock.FACING)) {
                    rotatedOffset = VectorUtils
                            .rotateVector2D(rotatedOffset, state.getValue(HorizontalBlock.FACING).getOpposite());
                }
                world.setBlockAndUpdate(pos.offset(rotatedOffset), SlaveTile.getSlaveBlock(name).get()
                        .defaultBlockState());
                TileEntity tile = world.getBlockEntity(pos.offset(rotatedOffset));
                if (tile instanceof MachineSlaveTile) {
                    MachineSlaveTile slave = (MachineSlaveTile) tile;
                    Vector3i mul = VectorUtils.mul(rotatedOffset, -1);
                    slave.getMasterOffset().set(mul);
                }
            }
        }
    }

    @Override
    public Trait clone() {
        return new MasterBlockTrait(this.name);
    }
}
