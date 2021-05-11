package software.bernie.techarium.network;

import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.techarium.machine.container.AutomaticContainer;

import java.util.Optional;

public abstract class ClientToServerContainerPacket<MSG extends ClientToServerContainerPacket<MSG>> extends Packet<MSG> {

    private int containerID = 0;
    //Empty dummy constructor
    protected ClientToServerContainerPacket() {}

    protected ClientToServerContainerPacket(AutomaticContainer container) {
        this.containerID = container.windowId;
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

    public Optional<AutomaticContainer> getContainer(NetworkEvent.Context context) {
        Container container = context.getSender().openContainer;
        if (container.windowId == containerID && container instanceof AutomaticContainer) {
            return Optional.of((AutomaticContainer) container);
        }
        return Optional.empty();
    }

    @Override
    final Optional<NetworkDirection> getDirection() {
        return Optional.of(NetworkDirection.PLAY_TO_SERVER);
    }
}
