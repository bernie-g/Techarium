package software.bernie.techarium.pipes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import software.bernie.techarium.pipes.networks.PipeNetwork;

import java.util.*;
import java.util.stream.Collectors;

public class PipeNetworkManagerCapability implements IPipeNetworkManagerCapability {

    private ServerWorld world;
    Map<UUID,PipeNetwork> networks = new HashMap<>();

    @CapabilityInject(IPipeNetworkManagerCapability.class)
    public static Capability<IPipeNetworkManagerCapability> INSTANCE = null;

    @Override
    public void tick(ServerWorld world) {
        this.world = world;
        List<PipeNetwork> toRemove = new ArrayList<>();
        getNetworks().forEach(pipeNetwork -> {if(pipeNetwork.tick(world)) toRemove.add(pipeNetwork);});
        toRemove.forEach(pipeNetwork -> deleteNetwork(pipeNetwork.getUuid()));
    }

    @Override
    public List<PipeNetwork> getNetworks() {
        return new ArrayList<>(networks.values());
    }

    @Override
    public ServerWorld getWorld() {
        return world;
    }

    @Override
    public void addNetwork(PipeNetwork network) {
        networks.put(network.getUuid(), network);
    }

    @Override
    public <T extends PipeNetwork> Optional<T> getNetwork(PipeType type, BlockPos pos) {
        return Optional.empty();
    }

    @Override
    public Optional<PipeNetwork> getByUUID(UUID uuid) {
        if (networks.containsKey(uuid))
            return Optional.of(networks.get(uuid));
        return Optional.empty();
    }

    @Override
    public void deleteNetwork(UUID networkUUID) {
        networks.remove(networkUUID);
    }

    @Override
    public ListNBT serializeNBT() {
        ListNBT nbt = new ListNBT();
        networks.values().forEach(pipeNetwork -> nbt.add(pipeNetwork.serializeNBT()));
        return nbt;
    }

    @Override
    public void deserializeNBT(ListNBT listNBT) {
        listNBT.forEach(nbt -> addNetwork(PipeNetwork.createFromNBT((CompoundNBT) nbt)));
    }
}
