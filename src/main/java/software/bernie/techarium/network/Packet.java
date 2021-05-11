package software.bernie.techarium.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class Packet<MSG extends Packet<MSG>> {

    protected Packet() {
    }

    abstract boolean isValid(NetworkEvent.Context context);

    abstract Optional<NetworkDirection> getDirection();


    abstract void write(PacketBuffer writeInto);

    abstract MSG create(PacketBuffer readFrom);

    /*
     * Run on dummy instance of Packet
     */
    void handle(MSG msg, Supplier<NetworkEvent.Context> context) {
        if(msg.isValid(context.get())) {
            context.get().enqueueWork(() -> msg.doAction(context.get()));
        }
        context.get().setPacketHandled(true);
    }

    /*
     * Executes Packet
     */
    abstract void doAction(NetworkEvent.Context context);
}
