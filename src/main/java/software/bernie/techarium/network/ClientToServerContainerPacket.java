package software.bernie.techarium.network;

import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Function;

public abstract class ClientToServerContainerPacket<MSG extends ClientToServerContainerPacket<MSG>> extends Packet<MSG> {

    private int containerID = 0;
    private Function<PacketBuffer,MSG> packetCreator;
    //Handler Constructor
    protected ClientToServerContainerPacket(Function<PacketBuffer,MSG> packetCreator) {
        this.packetCreator = packetCreator;
    }

    protected ClientToServerContainerPacket(Container container) {
        this.containerID = container.containerId;
    }

    protected ClientToServerContainerPacket(PacketBuffer buffer) {
        containerID = buffer.readInt();
    }

    @Override
    void write(PacketBuffer writeInto) {
        writeInto.writeInt(containerID);
    }
    @Override
    boolean isValid(NetworkEvent.Context context) {
        return true;
    }

    public Optional<Container> getContainer(NetworkEvent.Context context) {
        Container container = context.getSender().containerMenu;
        if (container.containerId == containerID) {
            return Optional.of(container);
        }
        return Optional.empty();
    }

    @Override
    final Optional<NetworkDirection> getDirection() {
        return Optional.of(NetworkDirection.PLAY_TO_SERVER);
    }

    MSG create(PacketBuffer readFrom) {
        return packetCreator.apply(readFrom);
    }
}
