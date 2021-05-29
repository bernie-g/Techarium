package software.bernie.techarium.pipes.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

public class PipeNetworkManagerStorage implements Capability.IStorage<IPipeNetworkManagerCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IPipeNetworkManagerCapability> capability, IPipeNetworkManagerCapability instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IPipeNetworkManagerCapability> capability, IPipeNetworkManagerCapability instance, Direction side, INBT nbt) {
        if (!(nbt instanceof ListNBT))
            throw new IllegalArgumentException("Unable to deserialize 'NetworkManager' Capability: " + nbt + "is not a CompoundNBT");
        instance.deserializeNBT((ListNBT) nbt);
    }
}
