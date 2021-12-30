package software.bernie.techarium.network.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import software.bernie.techarium.block.electrochromicglass.ElectroChromicGlassBlock;
import software.bernie.techarium.network.ServerToClientPacket;

public class ElectroChromicGlassPacket extends ServerToClientPacket<ElectroChromicGlassPacket> {

	private BlockPos pos;
	
	public ElectroChromicGlassPacket() {}
	
	public ElectroChromicGlassPacket(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public void write(PacketBuffer writeInto) {
		writeInto.writeBlockPos(pos);		
	}

	@Override
	public ElectroChromicGlassPacket create(PacketBuffer readFrom) {
		pos = readFrom.readBlockPos();
		return new ElectroChromicGlassPacket(pos);
	}

	@Override
	public void doAction(Context context) {
		context.enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			ClientWorld level = mc.level;
			BlockState state = level.getBlockState(pos);
			if (state.getBlock() instanceof ElectroChromicGlassBlock)
				level.setBlock(pos, state.setValue(ElectroChromicGlassBlock.POWERED, false), 8);
		});

		context.setPacketHandled(true);
	}

}