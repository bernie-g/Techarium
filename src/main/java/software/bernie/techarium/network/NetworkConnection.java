package software.bernie.techarium.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import software.bernie.techarium.Techarium;

public class NetworkConnection {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Techarium.ModID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private static int index = 0;

    public static void registerMessages() {
        registerMessage(new FluidTankClickContainerPacket());
        registerMessage(new EnergyBarClickContainerPacket());
    }
    public static <MSG extends Packet<MSG>> void registerMessage(MSG dummyPacket) {
        INSTANCE.registerMessage(getAndUpdateIndex(),
                (Class<MSG>) dummyPacket.getClass(),
                Packet::write,
                dummyPacket::create,
                dummyPacket::handle,
                dummyPacket.getDirection());
    }

    private static int getAndUpdateIndex() {
        return index++;
    }

}