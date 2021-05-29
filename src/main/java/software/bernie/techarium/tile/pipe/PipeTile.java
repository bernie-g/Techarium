package software.bernie.techarium.tile.pipe;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techarium.pipes.capability.IPipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeType;
import software.bernie.techarium.pipes.networks.PipeNetwork;
import software.bernie.techarium.registry.BlockTileRegistry;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class PipeTile extends TileEntity {
    public boolean isInput = false;
    private Map<PipeType, UUID> type = new EnumMap<>(PipeType.class);
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
        if (!world.isRemote()) {
            LazyOptional<IPipeNetworkManagerCapability> networkManagerCapability = world.getCapability(PipeNetworkManagerCapability.INSTANCE);
            if (networkManagerCapability.isPresent()) {
                IPipeNetworkManagerCapability manager = networkManagerCapability.orElseThrow(NullPointerException::new);
                for (Map.Entry<PipeType,UUID> uuid: type.entrySet()) {
                    Optional<PipeNetwork> network = manager.getByUUID(uuid.getValue());
                    if (network.isPresent()) {
                        type.put(uuid.getKey(), network.get().getNewUUID((ServerWorld)world, pos));
                    } else {
                        System.err.println("can't find pre-save network");
                    }
                }
            } else {
                System.err.println("can't find network manager");
            }
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putBoolean("isInput", isInput);
        ListNBT typeUUIDs = new ListNBT();
        for (Map.Entry<PipeType, UUID> uuid: type.entrySet()) {
            CompoundNBT elementNBT = new CompoundNBT();
            elementNBT.putInt("pipeType", uuid.getKey().ordinal());
            elementNBT.putUniqueId("uuid", uuid.getValue());
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
            type.put(PipeType.values()[compoundNBT.getInt("pipeType")], compoundNBT.getUniqueId("uuid"));
        }
    }
}
