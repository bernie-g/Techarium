package software.bernie.techarium.tile.pipe;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.block.pipe.PipeBlock;
import software.bernie.techarium.block.pipe.PipeData;
import software.bernie.techarium.pipes.PipePosition;
import software.bernie.techarium.pipes.capability.IPipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeType;
import software.bernie.techarium.pipes.networks.PipeNetwork;
import software.bernie.techarium.registry.BlockTileRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;


public class PipeTile extends TileEntity {
    public boolean isInput = false;
    private Map<PipeType, UUID> type = new EnumMap<>(PipeType.class);

    @Getter
    private PipeData displayData = new PipeData();
    public PipeTile() {
        super(BlockTileRegistry.PIPE.getTileEntityType());
    }

    public UUID getNetworkUUID(PipeType type) {
        return this.type.get(type);
    }

    public boolean isType(PipeType type) {
        return this.type.containsKey(type);
    }

    public boolean addType(PipeType type, UUID uuid) {
        if (this.type.containsKey(type))
            return false;
        this.type.put(type, uuid);
        return true;
    }

    public void updateUUID(PipeType type, UUID uuid) {
        this.type.put(type, uuid);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide()) {
            LazyOptional<IPipeNetworkManagerCapability> networkManagerCapability = level.getCapability(PipeNetworkManagerCapability.INSTANCE);
            if (networkManagerCapability.isPresent()) {
                IPipeNetworkManagerCapability manager = networkManagerCapability.orElseThrow(NullPointerException::new);
                for (Map.Entry<PipeType,UUID> uuid: type.entrySet()) {
                    Optional<PipeNetwork> network = manager.getByUUID(uuid.getValue());
                    if (network.isPresent()) {
                        type.put(uuid.getKey(), network.get().getNewUUID((ServerWorld)level, worldPosition));
                    } else {
                        LogManager.getLogger().error("can't find pre-save network");
                    }
                }
            } else {
                LogManager.getLogger().error("can't find network manager");
            }
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(this.getBlockState(), pkt.getTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return displayData.serialize();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        displayData = PipeData.deserialize(tag);
        requestModelDataUpdate();
    }



    @NotNull
    @Override
    public IModelData getModelData() {
        return super.getModelData();
    }

    public void updateDisplayState() {
        if (! (level instanceof ServerWorld))
            return;
        displayData = new PipeData();
        List<Pair<PipeType, PipeNetwork>> orderedTypes = orderedTypes();
        for (int i = 0; i < orderedTypes.size(); i++) {
            displayData.types[i] = orderedTypes.get(i).getLeft().ordinal() + 1;
            Map<Direction, UUID> surrounding = PipeBlock.getSurroundingNetworks(level, worldPosition, orderedTypes.get(i).getLeft());
            PipeNetwork network = orderedTypes.get(i).getRight();
            for (Direction direction: Direction.values()) {
                LazyOptional<?> capability = network.getCapability((ServerWorld) level, new PipePosition(worldPosition, direction));
                if (surrounding.containsKey(direction) || capability.isPresent()) {
                    displayData.pipeConnections.put(direction, displayData.pipeConnections.getOrDefault(direction, 0) + (int)Math.pow(2, i));
                 }
                if (capability.isPresent()) {
                    displayData.pipeEnds.put(direction, true);
                }
            }
        }
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
    }

    /**
     * Call only on ServerWorld
     * @return The sorted PipeConnections
     */
    private List<Pair<PipeType, PipeNetwork>> orderedTypes() {
        return type.entrySet().stream().sorted(Comparator.comparingInt(entry -> entry.getKey().ordinal())).map(entry -> new ImmutablePair<>(entry.getKey(),  level.getCapability(PipeNetworkManagerCapability.INSTANCE).orElseThrow(NullPointerException::new).getByUUID(entry.getValue()).get())).collect(Collectors.toList());
    }
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putBoolean("isInput", isInput);
        ListNBT typeUUIDs = new ListNBT();
        for (Map.Entry<PipeType, UUID> uuid: type.entrySet()) {
            CompoundNBT elementNBT = new CompoundNBT();
            elementNBT.putInt("pipeType", uuid.getKey().ordinal());
            elementNBT.putUUID("uuid", uuid.getValue());
            typeUUIDs.add(elementNBT);
        }
        nbt.put("typeUUIDs", typeUUIDs);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        isInput = nbt.getBoolean("isInput");
        ListNBT typeUUIDs = nbt.getList("typeUUIDs", Constants.NBT.TAG_COMPOUND);
        for (INBT elementNBT: typeUUIDs) {
            CompoundNBT compoundNBT = (CompoundNBT) elementNBT;
            type.put(PipeType.values()[compoundNBT.getInt("pipeType")], compoundNBT.getUUID("uuid"));
        }
    }
}
