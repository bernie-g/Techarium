package software.bernie.techarium.pipes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import software.bernie.techarium.pipes.networks.PipeNetwork;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PipeNetworkManagerCapability implements IPipeNetworkManagerCapability {

    @CapabilityInject(IPipeNetworkManagerCapability.class)
    public static Capability<IPipeNetworkManagerCapability> INSTANCE = null;



    @Override
    public List<PipeNetwork> getNetworks() {
        return null;
    }

    @Override
    public void addNetwork(PipeNetwork network) {

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
