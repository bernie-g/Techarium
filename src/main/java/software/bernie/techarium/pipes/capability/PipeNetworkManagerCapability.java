package software.bernie.techarium.pipes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.networks.EnergyPipeNetwork;
import software.bernie.techarium.pipes.networks.FluidPipeNetwork;
import software.bernie.techarium.pipes.networks.ItemPipeNetwork;
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
        getNetworks().forEach(pipeNetwork -> pipeNetwork.tick(world));
    }

    @Override
    public List<PipeNetwork> getNetworks() {
        return networks.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
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
    public Optional<PipeNetwork> getByUUIDorPos(UUID uuid, BlockPos pos) {
        if (networks.containsKey(uuid))
            return Optional.of(networks.get(uuid));
        return Optional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
