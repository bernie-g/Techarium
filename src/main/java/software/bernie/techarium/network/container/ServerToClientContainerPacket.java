package software.bernie.techarium.network.container;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.techarium.network.Packet;

import java.util.Optional;
import java.util.function.Function;

public abstract class ServerToClientContainerPacket<MSG extends ServerToClientContainerPacket<MSG>> extends Packet<MSG> {

    private int containerID = 0;
    private Function<PacketBuffer,MSG> packetCreator;

    protected ServerToClientContainerPacket(Function<PacketBuffer,MSG> packetCreator) {
        this.packetCreator = packetCreator;
    }

    protected ServerToClientContainerPacket(Container container) {
        this.containerID = container.containerId;
    }

    protected ServerToClientContainerPacket(PacketBuffer buffer) {
        containerID = buffer.readInt();
    }
    @Override
    public boolean isValid(NetworkEvent.Context context) {
        return true;
    }

    @Override
    public Optional<NetworkDirection> getDirection() {
        return Optional.of(NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public void write(PacketBuffer writeInto) {
        writeInto.writeInt(containerID);
    }

    @Override
    public MSG create(PacketBuffer readFrom) {
        return packetCreator.apply(readFrom);
    }

    public Optional<Container> getContainer(NetworkEvent.Context context) {
        Container container = Minecraft.getInstance().player.containerMenu;
        if (container.containerId == containerID) {
            return Optional.of(container);
        }
        return Optional.empty();
    }
}
