package software.bernie.techarium.pipes.networks;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.MutablePair;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.capability.PipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeType;
import software.bernie.techarium.tile.pipe.PipeTileEntity;

import java.util.*;

@Getter
@Setter
public abstract class PipeNetwork<Cap, ToTransport> {

    private UUID uuid;
    private List<BlockPos> pipeBlocks = new ArrayList<>();
    private List<PipePosition> inputs = new ArrayList<>();
    private List<PipePosition> outputs = new ArrayList<>();

    private boolean isDeprecated = false;
    private Map<BlockPos, UUID> newUUID = new HashMap<>();

    public abstract PipeType getType();

    /**
     * @param world
     * @return if the network should be removed after that tick
     */
    public boolean tick(ServerWorld world) {
        if (isDeprecated || pipeBlocks.isEmpty()) {
            if (newUUID.isEmpty()) {
                return true;
            }
            //Don't remove, but also don't delete network information. There are still unsaved network information in here
            return false;
        }
        inputs.forEach(input -> {
            LazyOptional<Cap> inputCap = getCapability(world, input);
            if (inputCap.isPresent()) {
                executeInput(world, input, inputCap.orElseThrow(NullPointerException::new));
            }
        });
        return false;
    }

    public void deprecateAll(ServerWorld world, UUID newNetworkUUID) {
        isDeprecated = true;
        for (BlockPos pipePos: pipeBlocks) {
            if (world.getChunkProvider().isChunkLoaded(new ChunkPos(pipePos))) {
                TileEntity te = world.getTileEntity(pipePos);
                if (te instanceof PipeTileEntity) {
                    ((PipeTileEntity)te).updateUUID(getType(), newNetworkUUID);
                }
            } else { //If not loaded put it into the map and on the next PipeTileEntity#onLoad call it will get updated
                newUUID.put(pipePos, newNetworkUUID);
            }
        }
    }

    public void deprecateSpecific(ServerWorld world, Map<BlockPos, UUID> newNetworkUUIDs) {
        isDeprecated = true;
        if (newNetworkUUIDs.size() != pipeBlocks.size()) {
            throw new IllegalArgumentException("Not all blocks are deprecated " + newNetworkUUIDs.size() + "," + pipeBlocks.size());
        }
        for (Map.Entry<BlockPos, UUID> data: newNetworkUUIDs.entrySet()) {
            if (world.getChunkProvider().isChunkLoaded(new ChunkPos(data.getKey()))) {
                TileEntity te = world.getTileEntity(data.getKey());
                if (te instanceof PipeTileEntity) {
                    ((PipeTileEntity)te).updateUUID(getType(), data.getValue());
                }
            } else { //If not loaded put it into the map and on the next PipeTileEntity#onLoad call it will get updated
                newUUID.put(data.getKey(), data.getValue());
            }
        }
    }

    public UUID getNewUUID(ServerWorld world, BlockPos pos) {
        if (isDeprecated) {
            UUID retUUID = newUUID.get(pos);
            newUUID.remove(pos);
            if (newUUID.isEmpty()) {
                world.getCapability(PipeNetworkManagerCapability.INSTANCE).ifPresent(manager -> manager.deleteNetwork(uuid));
            }
            return retUUID;
        }
        return uuid;
    }

    private void executeInput(ServerWorld world, PipePosition inputPos, Cap inputCap) {
        for (int i = 0; i < getSlots(inputCap); i++) {
            ToTransport maxDrained = drain(inputCap, getMaxRemove(), i, true);
            if (isEmpty(maxDrained) || !getFilter(inputPos).canPassThrough(maxDrained))
                continue;
            for (MutablePair<Cap,PipePosition> output : getOrderedCapability(world)) {
                if (!getFilter(output.getRight()).canPassThrough(maxDrained))
                    continue; // fill next output
                ToTransport filled = fill(output.getLeft(), maxDrained, false);
                if (isEmpty(filled))
                    continue; // fill next output
                drainWith(inputCap, filled, i, false);
                return; //Do the next input
            }
            //could not fill the input to network, so try next slot
        }

    }

    //Add ordering Logic here
    private List<MutablePair<Cap, PipePosition>> getOrderedCapability(ServerWorld world) {
        List<MutablePair<Cap, PipePosition>> caps = new ArrayList<>();
        for (PipePosition outputPos : outputs) {
            LazyOptional<Cap> output = getCapability(world, outputPos);
            output.ifPresent(cap -> caps.add(new MutablePair<>(cap, outputPos)));
        }
        return caps;
    }

    public abstract Filter<ToTransport> getFilter(PipePosition pipePosition);

    public LazyOptional<Cap> getCapability(ServerWorld world, PipePosition position) {
        if (world.getChunkProvider().isChunkLoaded(new ChunkPos(position.getPos()))) {
            TileEntity te =  world.getTileEntity(position.getPos().offset(position.getDirection()));
            if (te != null) {
                return te.getCapability(getDefaultCapability(), position.getDirection());
            }
        }
        return LazyOptional.empty();
    }

    public abstract boolean isEmpty(ToTransport toTransport);

    public abstract Capability<Cap> getDefaultCapability();

    public abstract int getMaxRemove();

    public abstract boolean canFill(Cap capability, ToTransport transport);

    public abstract ToTransport drain(Cap capability, int amount, int slot, boolean simulate);

    public abstract ToTransport drainWith(Cap capability, ToTransport drain, int slot, boolean simulate);

    public abstract ToTransport fill(Cap capability, ToTransport transport, boolean simulate);

    public abstract int getSlots(Cap capability);
}
