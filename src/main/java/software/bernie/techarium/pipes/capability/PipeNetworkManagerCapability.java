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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PipeNetworkManagerCapability implements IPipeNetworkManagerCapability {

    private ServerWorld world;
    List<PipeNetwork> networks = new ArrayList<>();

    @CapabilityInject(IPipeNetworkManagerCapability.class)
    public static Capability<IPipeNetworkManagerCapability> INSTANCE = null;

    public PipeNetworkManagerCapability() {
        ItemPipeNetwork itemPipeNetwork = new ItemPipeNetwork();
        itemPipeNetwork.getInputs().add(new PipePosition(new BlockPos(0,57,0), Direction.EAST));
        itemPipeNetwork.getOutputs().add(new PipePosition(new BlockPos(0,57,0), Direction.WEST));
        itemPipeNetwork.getOutputs().add(new PipePosition(new BlockPos(0,57,0), Direction.NORTH));
        itemPipeNetwork.getOutputs().add(new PipePosition(new BlockPos(0,57,0), Direction.SOUTH));
        networks.add(itemPipeNetwork);
        EnergyPipeNetwork energyPipeNetwork = new EnergyPipeNetwork();
        energyPipeNetwork.getInputs().add(new PipePosition(new BlockPos(0,57,0), Direction.EAST));
        energyPipeNetwork.getOutputs().add(new PipePosition(new BlockPos(0,57,0), Direction.WEST));
        energyPipeNetwork.getOutputs().add(new PipePosition(new BlockPos(0,57,0), Direction.NORTH));
        energyPipeNetwork.getOutputs().add(new PipePosition(new BlockPos(0,57,0), Direction.SOUTH));
        networks.add(energyPipeNetwork);
        FluidPipeNetwork fluidPipeNetwork = new FluidPipeNetwork();
        fluidPipeNetwork.getInputs().add(new PipePosition(new BlockPos(0,57,0), Direction.EAST));
        fluidPipeNetwork.getOutputs().add(new PipePosition(new BlockPos(0,57,0), Direction.WEST));
        fluidPipeNetwork.getOutputs().add(new PipePosition(new BlockPos(0,57,0), Direction.NORTH));
        fluidPipeNetwork.getOutputs().add(new PipePosition(new BlockPos(0,57,0), Direction.SOUTH));
        networks.add(fluidPipeNetwork);
    }

    @Override
    public void tick(ServerWorld world) {
        this.world = world;
        getNetworks().forEach(pipeNetwork -> pipeNetwork.tick(world));
    }

    @Override
    public List<PipeNetwork> getNetworks() {
        return networks;
    }

    @Override
    public void addNetwork(PipeNetwork network) {
        networks.add(network);
    }

    @Override
    public <T extends PipeNetwork> Optional<T> getNetwork(PipeType type, BlockPos pos) {
        return Optional.empty();
    }

    @Override
    public <T extends PipeNetwork> Optional<T> getByUUIDorPos(UUID uuid, BlockPos pos) {
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
