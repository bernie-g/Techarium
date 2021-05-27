package software.bernie.techarium.pipes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.networks.ItemPipeNetwork;
import software.bernie.techarium.pipes.networks.PipeNetwork;
import software.bernie.techarium.tile.pipe.PipeTileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPipeNetworkManagerCapability extends INBTSerializable<CompoundNBT> {
    void tick(ServerWorld world);

    @NotNull
    List<PipeNetwork> getNetworks();

    ServerWorld getWorld();

    void addNetwork(PipeNetwork network);

    <T extends PipeNetwork> Optional<T> getNetwork(PipeType type, BlockPos pos);
    <T extends PipeNetwork> Optional<T> getByUUIDorPos(UUID uuid, BlockPos pos);

    default UUID createNetwork(BlockPos pos, PipeTileEntity tileEntity) {
        UUID networkUUID = UUID.randomUUID();
        ItemPipeNetwork network = new ItemPipeNetwork();
        network.setUuid(networkUUID);
        if (tileEntity.isInput) {
            network.getInputs().addAll(generateAll(pos));
        } else {
            network.getOutputs().addAll(generateAll(pos));
        }
        addNetwork(network);
        return networkUUID;
    }

    default void appendToNetwork(BlockPos pos, PipeTileEntity tileEntity, UUID networkUUID, Direction direction) {
        Optional<PipeNetwork> networkOptional = getByUUIDorPos(networkUUID, pos.offset(direction));
        if (networkOptional.isPresent()) {
            PipeNetwork network = networkOptional.get();
            if (tileEntity.isInput) {
                network.getInputs().addAll(generateAll(pos));
            } else {
                network.getOutputs().addAll(generateAll(pos));
            }
        } else {
            System.out.println("Could not find pipe network @ " + pos.offset(direction) + " with ID: " + networkUUID);
        }
    }
    default List<PipePosition> generateAll(BlockPos pos) {
        List<PipePosition> list = new ArrayList<>();
        for (Direction direction: Direction.values()) {
            list.add(new PipePosition(pos, direction));
        }
        return list;
    }
}
