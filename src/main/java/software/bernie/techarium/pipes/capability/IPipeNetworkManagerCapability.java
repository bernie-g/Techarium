package software.bernie.techarium.pipes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.networks.EnergyPipeNetwork;
import software.bernie.techarium.pipes.networks.FluidPipeNetwork;
import software.bernie.techarium.pipes.networks.ItemPipeNetwork;
import software.bernie.techarium.pipes.networks.PipeNetwork;
import software.bernie.techarium.tile.pipe.PipeTileEntity;

import java.util.*;
import java.util.stream.Collectors;

public interface IPipeNetworkManagerCapability extends INBTSerializable<CompoundNBT> {
    void tick(ServerWorld world);

    @NotNull
    List<PipeNetwork> getNetworks();

    ServerWorld getWorld();

    void addNetwork(PipeNetwork network);

    <T extends PipeNetwork> Optional<T> getNetwork(PipeType type, BlockPos pos);
    <T extends PipeNetwork> Optional<T> getByUUID(UUID uuid);

    default UUID createNetwork(BlockPos pos, PipeTileEntity tileEntity, PipeType type) {
        UUID networkUUID = UUID.randomUUID();
        PipeNetwork network;
        switch (type) {
            case ITEM:
                network = new ItemPipeNetwork();
                break;
            case FLUID:
                network = new FluidPipeNetwork();
                break;
            case ENERGY:
                network = new EnergyPipeNetwork();
                break;
            default:
                throw new UnsupportedOperationException("PipeType not supported: " + type);
        }
        network.setUuid(networkUUID);
        if (tileEntity.isInput) {
            network.getInputs().addAll(generateAll(pos));
        } else {
            network.getOutputs().addAll(generateAll(pos));
        }
        network.getPipeBlocks().add(pos);
        addNetwork(network);
        return networkUUID;
    }

    default void appendToNetwork(BlockPos pos, PipeTileEntity tileEntity, UUID networkUUID) {
        Optional<PipeNetwork> networkOptional = getByUUID(networkUUID);
        if (networkOptional.isPresent()) {
            PipeNetwork network = networkOptional.get();
            network.getPipeBlocks().add(pos);
            if (tileEntity.isInput) {
                network.getInputs().addAll(generateAll(pos));
            } else {
                network.getOutputs().addAll(generateAll(pos));
            }
        } else {
            System.err.println("Could not find pipe network @ " + pos + " with ID: " + networkUUID);
        }
    }

    default UUID mergeNetworks(ServerWorld world, BlockPos pos, PipeTileEntity tileEntity, List<UUID> networkUUIDs) {
        if(networkUUIDs.isEmpty())
            return UUID.randomUUID();
        List<PipeNetwork> networks = networkUUIDs.stream().map(this::getByUUID).filter(Optional::isPresent).map(optional -> optional.orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
        UUID networkUUID = createNetwork(pos, tileEntity, networks.get(0).getType());
        PipeNetwork network = getByUUID(networkUUID).get();
        networks.forEach(oldNetwork -> {
            network.getPipeBlocks().addAll(oldNetwork.getPipeBlocks());
            network.getInputs().addAll(oldNetwork.getInputs());
            network.getOutputs().addAll(oldNetwork.getOutputs());
            oldNetwork.deprecateAll(world, networkUUID);
        });
        return networkUUID;
    }

    void deleteNetwork(UUID networkUUID);

    default void removeFromNetwork(BlockPos pos, UUID networkUUID) {
        Optional<PipeNetwork> networkOptional = getByUUID(networkUUID);
        if (networkOptional.isPresent()) {
            PipeNetwork network = networkOptional.get();
            network.getPipeBlocks().removeIf(o -> o.equals(pos));
            network.getInputs().removeIf(o -> o.equals(pos));
            network.getOutputs().removeIf(o -> o.equals(pos));
        } else {
            System.err.println("Could not delete network with ID: " + networkUUID);
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
