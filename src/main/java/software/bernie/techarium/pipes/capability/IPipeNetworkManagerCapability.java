package software.bernie.techarium.pipes.capability;

import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.networks.PipeNetwork;
import software.bernie.techarium.tile.pipe.PipeTile;
import org.apache.logging.log4j.LogManager;
import java.util.*;
import java.util.stream.Collectors;

public interface IPipeNetworkManagerCapability extends INBTSerializable<ListNBT> {

    void tick(ServerWorld world);

    @NotNull
    List<PipeNetwork> getNetworks();

    ServerWorld getWorld();

    void addNetwork(PipeNetwork network);

    <T extends PipeNetwork> Optional<T> getNetwork(PipeType type, BlockPos pos);
    <T extends PipeNetwork> Optional<T> getByUUID(UUID uuid);

    default UUID createNetwork(PipeType type) {
        UUID networkUUID = UUID.randomUUID();
        PipeNetwork network = PipeNetwork.createNetwork(type);
        network.setUuid(networkUUID);
        addNetwork(network);
        return networkUUID;
    }

    default void appendToNetwork(BlockPos pos, PipeTile tileEntity, UUID networkUUID) {
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
            LogManager.getLogger().error("Could not find pipe network @ " + pos + " with ID: " + networkUUID);
        }
    }

    default UUID mergeNetworks(ServerWorld world, BlockPos pos, PipeTile tileEntity, List<UUID> networkUUIDs) {
        if(networkUUIDs.isEmpty())
            return UUID.randomUUID();
        List<PipeNetwork> networks = networkUUIDs.stream().map(this::getByUUID).filter(Optional::isPresent).map(optional -> optional.orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
        UUID networkUUID = createNetwork(networks.get(0).getType());
        appendToNetwork(pos, tileEntity, networkUUID);
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
            LogManager.getLogger().error("Could not delete network with ID: " + networkUUID);
        }
    }

    default void splitNetwork(BlockPos pos, Set<Direction> connected, UUID networkUUID) {
        Map<Direction, List<BlockPos>> newNetworks = rebuildNetwork(pos, connected, networkUUID);
        if (newNetworks.size() == 1) {
            return;
            //Everything is connected: don't create new Networks
        }
        PipeNetwork oldNetwork = getByUUID(networkUUID).get();
        Map<BlockPos, UUID> deprecationData = new HashMap<>();
        for (List<BlockPos> newNetwork: newNetworks.values()) {
            UUID uuid = createNetwork(oldNetwork.getType());
            PipeNetwork network = getByUUID(uuid).get();
            oldNetwork.getInputs().stream().filter(o -> isPartOfNetwork((PipePosition)o, newNetwork)).forEach(o -> network.getInputs().add(o));
            oldNetwork.getOutputs().stream().filter(o -> isPartOfNetwork((PipePosition)o, newNetwork)).forEach(o -> network.getOutputs().add(o));
            network.getPipeBlocks().addAll(newNetwork);
            network.getPipeBlocks().forEach(tempPos -> deprecationData.put((BlockPos)tempPos, uuid));
        }
        oldNetwork.deprecateSpecific(getWorld(), deprecationData);
    }

    static boolean isPartOfNetwork(PipePosition position, List<BlockPos> network) {
        return network.stream().filter(position::equals).count() > 0;
    }

    default Map<Direction, List<BlockPos>> rebuildNetwork(BlockPos pos, Collection<Direction> connected, UUID networkUUID) {
        Optional<PipeNetwork> networkOptional = getByUUID(networkUUID);
        if (networkOptional.isPresent()) {
            PipeNetwork network = networkOptional.get();
            List<BlockPos> pipeBlocks = network.getPipeBlocks();
            List<Direction> alreadyConnected = new ArrayList<>();
            Map<Direction, List<BlockPos>> newNetworks = new EnumMap<>(Direction.class);
            for (Direction direction : connected) {
                if (alreadyConnected.contains(direction))
                    continue;
                int preSize = 0;
                List<BlockPos> connectedInDirection = new ArrayList<>();
                connectedInDirection.add(pos.relative(direction));
                alreadyConnected.add(direction);
                while (connectedInDirection.size() != preSize) {
                    preSize = connectedInDirection.size();
                    for (BlockPos toTest: pipeBlocks) {
                        if (connectedInDirection.contains(toTest))
                            continue;
                        if (isConnectedTo(toTest, connectedInDirection)) {
                            connectedInDirection.add(toTest);
                            if (toTest.distManhattan(pos) == 1) {
                                Direction manhattanDirection = getManhattanConnection(pos, toTest);
                                if (!alreadyConnected.contains(manhattanDirection)) {
                                    alreadyConnected.add(manhattanDirection);
                                }
                            }
                        }
                    }
                }
                newNetworks.put(direction, connectedInDirection);
            }
            return newNetworks;
        } else {
            LogManager.getLogger().error("Could not delete network with ID: " + networkUUID);
        }
        return new HashMap<>();
    }

    static boolean isConnectedTo(BlockPos toTest, List<BlockPos> network) {
        for (BlockPos networkPos: network) {
            if (networkPos.distManhattan(toTest) == 1)
                return true;
        }
        return false;
    }

    static Direction getManhattanConnection(BlockPos origin, BlockPos toTest) {
        for (Direction direction: Direction.values()) {
            if (origin.relative(direction).equals(toTest))
                return direction;
        }
        throw new IllegalArgumentException("the BlockPositions don't connect");
    }

    default List<PipePosition> generateAll(BlockPos pos) {
        List<PipePosition> list = new ArrayList<>();
        for (Direction direction: Direction.values()) {
            list.add(new PipePosition(pos, direction));
        }
        return list;
    }
}
