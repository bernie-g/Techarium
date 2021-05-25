package software.bernie.techarium.pipes.networks;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.capability.PipeType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PACKAGE)
public abstract class PipeNetwork<Cap, ToTransport> {

    private UUID uuid;
    private List<BlockPos> pipeBlocks = new ArrayList<>();
    private List<PipePosition> inputs = new ArrayList<>();
    private List<PipePosition> either = new ArrayList<>();
    private List<PipePosition> outputs = new ArrayList<>();

    public abstract boolean isType(PipeType type);

    public abstract void tick();

    public LazyOptional<Cap> getCapability(ServerWorld world, PipePosition position) {
        if (world.getChunkProvider().isChunkLoaded(new ChunkPos(position.getPos()))) {
            TileEntity te =  world.getTileEntity(position.getPos().offset(position.getDirection()));
            if (te != null) {
                return te.getCapability(getDefaultCapability(), position.getDirection());
            }
        }
        return LazyOptional.empty();
    }

    public abstract Capability<Cap> getDefaultCapability();

    public abstract boolean canFill(Cap capability, ToTransport transport);

    public abstract ToTransport drain(Cap capability, int amount, int slot, boolean simulate);

    public abstract ToTransport fill(Cap capability, ToTransport transport, boolean simulate);

    public abstract int getSlots(Cap capability);
}
