package software.bernie.techarium.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.techarium.util.LogCache;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class Packet<MSG extends Packet<MSG>> {

    protected Packet() {
    }

    public abstract boolean isValid(NetworkEvent.Context context);

    public abstract Optional<NetworkDirection> getDirection();


    public abstract void write(PacketBuffer writeInto);

    public abstract MSG create(PacketBuffer readFrom);

    /*
     * Run on dummy instance of Packet
     */
    public void handle(MSG msg, Supplier<NetworkEvent.Context> context) {
        if (msg.isValid(context.get())) {
            context.get().enqueueWork(() -> msg.doAction(context.get()));
        } else {
            LogCache.getLogger(Packet.class).warn("invalid packet " + (context.get().getSender() == null ? "from the server" : ("by " + context.get().getSender().getName().getContents() + " with IP-Address " + context.get().getSender().getIpAddress())));
        }
        context.get().setPacketHandled(true);
    }

    /*
     * Executes Packet
     */
    public abstract void doAction(NetworkEvent.Context context);
}
