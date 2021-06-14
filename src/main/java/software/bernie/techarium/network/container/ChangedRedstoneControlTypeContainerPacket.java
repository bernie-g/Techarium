package software.bernie.techarium.network.container;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.techarium.display.container.PipeContainer;
import software.bernie.techarium.pipe.util.RedstoneControlType;

public class ChangedRedstoneControlTypeContainerPacket extends ClientToServerContainerPacket<ChangedRedstoneControlTypeContainerPacket> {

    RedstoneControlType type;
    boolean input;

    public ChangedRedstoneControlTypeContainerPacket() {
        super(ChangedRedstoneControlTypeContainerPacket::new);
    }

    public ChangedRedstoneControlTypeContainerPacket(PipeContainer container, RedstoneControlType type, boolean input) {
        super(container);
        this.type = type;
        this.input = input;
    }

    public ChangedRedstoneControlTypeContainerPacket(PacketBuffer buffer) {
        super(buffer);
        type = RedstoneControlType.values()[buffer.readInt()];
        input = buffer.readBoolean();
    }

    @Override
    public void write(PacketBuffer writeInto) {
        super.write(writeInto);
        writeInto.writeInt(type.ordinal());
        writeInto.writeBoolean(input);
    }

    @Override
    public void doAction(NetworkEvent.Context context) {
        getContainer(context).ifPresent(tempContainer -> {
            if (!(tempContainer instanceof PipeContainer))
                return;
            PipeContainer container = (PipeContainer) tempContainer;
            container.getPipeTile().ifPresent(pipeTile ->
                pipeTile.getConfig().getConfigBy(input).get((container.tilePos.getDirection())).setRedstoneControlType(type)
            );
        });
    }
}
