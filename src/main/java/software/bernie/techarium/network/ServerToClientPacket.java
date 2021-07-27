package software.bernie.techarium.network;

import java.util.Optional;

import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public abstract class ServerToClientPacket<MSG extends ServerToClientPacket<MSG>> extends Packet<MSG>{

	@Override
	public boolean isValid(Context context) {
		return getDirection().isPresent();
	}

	@Override
	public Optional<NetworkDirection> getDirection() {
		return Optional.of(NetworkDirection.PLAY_TO_CLIENT);
	}

}
