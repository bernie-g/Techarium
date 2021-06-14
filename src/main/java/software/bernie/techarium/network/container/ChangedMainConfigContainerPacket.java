package software.bernie.techarium.network.container;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.techarium.display.container.PipeContainer;
import software.bernie.techarium.display.screen.widget.pipe.MainConfigWidget;
import software.bernie.techarium.pipe.util.PipeMainConfig;
import software.bernie.techarium.pipe.util.RedstoneControlType;

public class ChangedMainConfigContainerPacket extends ClientToServerContainerPacket<ChangedMainConfigContainerPacket> {

    PipeMainConfig config;

    public ChangedMainConfigContainerPacket() {
        super(ChangedMainConfigContainerPacket::new);
    }

    public ChangedMainConfigContainerPacket(PipeContainer container, PipeMainConfig config) {
        super(container);
        this.config = config;
    }

    public ChangedMainConfigContainerPacket(PacketBuffer buffer) {
        super(buffer);
        config = new PipeMainConfig();
        config.setInput(buffer.readBoolean());
        config.setOutput(buffer.readBoolean());
        config.setRoundRobin(buffer.readBoolean());
        config.setSelfFeed(buffer.readBoolean());
    }

    @Override
    public void write(PacketBuffer writeInto) {
        super.write(writeInto);
        writeInto.writeBoolean(config.isInput());
        writeInto.writeBoolean(config.isOutput());
        writeInto.writeBoolean(config.isRoundRobin());
        writeInto.writeBoolean(config.isSelfFeed());
    }

    @Override
    public void doAction(NetworkEvent.Context context) {
        getContainer(context).ifPresent(tempContainer -> {
            if (!(tempContainer instanceof PipeContainer))
                return;
            PipeContainer container = (PipeContainer) tempContainer;
            container.getPipeTile().ifPresent(pipeTile -> {
                pipeTile.getConfig().getMainConfig().get((container.tilePos.getDirection())).setInput(config.isInput());
                pipeTile.getConfig().getMainConfig().get((container.tilePos.getDirection())).setOutput(config.isOutput());
                pipeTile.getConfig().getMainConfig().get((container.tilePos.getDirection())).setRoundRobin(config.isRoundRobin());
                pipeTile.getConfig().getMainConfig().get((container.tilePos.getDirection())).setSelfFeed(config.isSelfFeed());
                }
            );
        });
    }
}
