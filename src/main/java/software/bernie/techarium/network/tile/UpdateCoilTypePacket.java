package software.bernie.techarium.network.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import software.bernie.techarium.block.coils.MagneticCoilType;
import software.bernie.techarium.display.container.ExchangeStationContainer;
import software.bernie.techarium.network.ServerToClientPacket;
import software.bernie.techarium.network.container.RecipeWidgetClickContainerPacket;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;

public class UpdateCoilTypePacket extends ServerToClientPacket<UpdateCoilTypePacket>{
	
	private BlockPos pos;
	private MagneticCoilType type;

    public UpdateCoilTypePacket() {}
	
	public UpdateCoilTypePacket(BlockPos pos, MagneticCoilType type) {
		this.pos = pos;
		this.type = type;
	}
	
	@Override
	public void write(PacketBuffer writeInto) {
		writeInto.writeBlockPos(pos);
		writeInto.writeInt(type.ordinal());
	}
	
	@Override
	public UpdateCoilTypePacket create(PacketBuffer readFrom) {
		pos = readFrom.readBlockPos();
		type = MagneticCoilType.values()[readFrom.readInt()];
		return new UpdateCoilTypePacket(pos, type);
	}

	@Override
	public void doAction(Context context) {
		context.enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			ClientWorld level = mc.level;
			if (level != null) {
				TileEntity te = level.getBlockEntity(pos);
				
				if (te instanceof MagneticCoilTile) {
					((MagneticCoilTile) te).setCoilType(type);
				}
			}
		});

		context.setPacketHandled(true);
	}
}
