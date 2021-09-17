package software.bernie.techarium.network.container;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.tile.sync.TechariumDataSlot;

import java.util.List;
import java.util.Optional;

public class SyncContainerPacket extends ServerToClientContainerPacket<SyncContainerPacket> {
    CompoundNBT syncNBT = new CompoundNBT();
    public SyncContainerPacket() {
        super(SyncContainerPacket::new);
    }

    public SyncContainerPacket(AutomaticContainer container) {
        super(container);
        ListNBT listNBT = new ListNBT();
        List<TechariumDataSlot<?>> dataSlots = container.getTile().getDataSlots();
        for (int i = 0; i < dataSlots.size(); i++) {
            TechariumDataSlot<?> dataSlot = dataSlots.get(i);
            if (dataSlot.getMode() == TechariumDataSlot.SyncMode.RENDER)
                continue;
            Optional<CompoundNBT> optionalNBT = dataSlot.toOptionalNBT();
            if (optionalNBT.isPresent()) {
                CompoundNBT data = optionalNBT.get();
                data.putInt("dataSlotIndex", i);
                listNBT.add(data);
                dataSlots.get(i).updatePrevValue();
            }
        }
        syncNBT.put("data", listNBT);
    }

    public SyncContainerPacket(PacketBuffer readFrom) {
        super(readFrom);
        syncNBT = readFrom.readNbt();
    }

    @Override
    public void write(PacketBuffer writeInto) {
        super.write(writeInto);
        writeInto.writeNbt(syncNBT);
    }

    @Override
    public void doAction(NetworkEvent.Context context) {
        getContainer(context).ifPresent(container -> {
            if (container instanceof AutomaticContainer) {
                AutomaticContainer automaticContainer = (AutomaticContainer) container;
                automaticContainer.getTile().onDataPacket(context.getNetworkManager(),
                        new SUpdateTileEntityPacket(automaticContainer.getTile().getBlockPos(), -1, syncNBT));
            }
        });
    }
}
