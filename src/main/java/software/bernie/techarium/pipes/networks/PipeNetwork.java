package software.bernie.techarium.pipes.networks;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.MutablePair;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.capability.PipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeType;
import software.bernie.techarium.tile.pipe.PipeTile;

import java.util.*;

@Getter
@Setter
public abstract class PipeNetwork<Cap, ToTransport> implements INBTSerializable<CompoundNBT> {

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

    //Add ordering Logic here
    private List<MutablePair<Cap, PipePosition>> getOrderedCapability(ServerWorld world) {
        List<MutablePair<Cap, PipePosition>> caps = new ArrayList<>();
        for (PipePosition outputPos : outputs) {
            LazyOptional<Cap> output = getCapability(world, outputPos);
            output.ifPresent(cap -> caps.add(new MutablePair<>(cap, outputPos)));
        }
        return caps;
    }

    public void deprecateAll(ServerWorld world, UUID newNetworkUUID) {
        isDeprecated = true;
        for (BlockPos pipePos: pipeBlocks) {
            if (world.getChunkSource().isEntityTickingChunk(new ChunkPos(pipePos))) {
                TileEntity te = world.getBlockEntity(pipePos);
                if (te instanceof PipeTile) {
                    ((PipeTile)te).updateUUID(getType(), newNetworkUUID);
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
            if (world.getChunkSource().isEntityTickingChunk(new ChunkPos(data.getKey()))) {
                TileEntity te = world.getBlockEntity(data.getKey());
                if (te instanceof PipeTile) {
                    ((PipeTile)te).updateUUID(getType(), data.getValue());
                }
            } else { //If not loaded put it into the map and on the next PipeTileEntity#onLoad call it will get updated
                newUUID.put(data.getKey(), data.getValue());
            }
        }
    }

    public abstract Filter<ToTransport> getFilter(PipePosition pipePosition);

    public LazyOptional<Cap> getCapability(ServerWorld world, PipePosition position) {
        if (world.getChunkSource().isEntityTickingChunk(new ChunkPos(position.getPos()))) {
            TileEntity te =  world.getBlockEntity(position.getPos().relative(position.getDirection()));
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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("type", getType().ordinal());
        nbt.putUUID("uuid", uuid);
        ListNBT pipeBlocksNBT = new ListNBT();
        pipeBlocks.forEach(blockPos -> pipeBlocksNBT.add(NBTUtil.writeBlockPos(blockPos)));
        nbt.put("pipeBlocks", pipeBlocksNBT);
        ListNBT inputNBT = new ListNBT();
        inputs.forEach(pipePos -> inputNBT.add(pipePos.serializeNBT()));
        nbt.put("inputs", inputNBT);
        ListNBT outputNBT = new ListNBT();
        outputs.forEach(pipePos -> outputNBT.add(pipePos.serializeNBT()));
        nbt.put("outputs", outputNBT);
        nbt.putBoolean("deprecated", isDeprecated);
        ListNBT newUUIDNBT = new ListNBT();
        newUUID.forEach((blockPos, tempUUID) -> {
            CompoundNBT tempNbt = NBTUtil.writeBlockPos(blockPos);
            tempNbt.putUUID("uuid", tempUUID);
            newUUIDNBT.add(tempNbt);
        });
        nbt.put("newUUID", newUUIDNBT);
        return nbt;
    }

    public static PipeNetwork createNetwork(PipeType type) {
        switch (type) {
            case ITEM:
                return new ItemPipeNetwork();
            case FLUID:
                return new FluidPipeNetwork();
            case ENERGY:
                return new EnergyPipeNetwork();
        }
        throw new UnsupportedOperationException("PipeType not supported: " + type);
    }

    public static PipeNetwork createFromNBT(CompoundNBT nbt) {
        PipeType type = PipeType.values()[nbt.getInt("type")];
        PipeNetwork network = createNetwork(type);
        network.deserializeNBT(nbt);
        return network;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        uuid = nbt.getUUID("uuid");
        ListNBT pipeBlocksNBT = nbt.getList("pipeBlocks", Constants.NBT.TAG_COMPOUND);
        pipeBlocksNBT.forEach(inbt -> pipeBlocks.add(NBTUtil.readBlockPos((CompoundNBT)inbt)));
        ListNBT inputNBT = nbt.getList("inputs", Constants.NBT.TAG_COMPOUND);
        inputNBT.forEach(inbt -> inputs.add(PipePosition.createFromNBT((CompoundNBT) inbt)));
        ListNBT outputNBT = nbt.getList("outputs", Constants.NBT.TAG_COMPOUND);
        outputNBT.forEach(inbt -> outputs.add(PipePosition.createFromNBT((CompoundNBT) inbt)));
        isDeprecated = nbt.getBoolean("deprecated");
        ListNBT newUUIDNBT = nbt.getList("newUUID", Constants.NBT.TAG_COMPOUND);
        newUUIDNBT.forEach(inbt -> {
            CompoundNBT compoundNBT = (CompoundNBT) inbt;
            newUUID.put(NBTUtil.readBlockPos(compoundNBT), compoundNBT.getUUID("uuid"));
        });
    }
}
