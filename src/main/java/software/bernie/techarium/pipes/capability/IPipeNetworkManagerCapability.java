package software.bernie.techarium.pipes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.pipes.networks.PipeNetwork;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPipeNetworkManagerCapability extends INBTSerializable<CompoundNBT> {

    @NotNull
    List<PipeNetwork> getNetworks();

    void addNetwork(PipeNetwork network);

    <T extends PipeNetwork> Optional<T> getNetwork(PipeType type, BlockPos pos);
    <T extends PipeNetwork> Optional<T> getByUUIDorPos(UUID uuid, BlockPos pos);
}
