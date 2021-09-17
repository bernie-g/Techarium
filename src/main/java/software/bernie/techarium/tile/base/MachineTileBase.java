package software.bernie.techarium.tile.base;

import java.util.*;
import java.util.stream.Stream;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.DirectionProperty;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.network.NetworkConnection;
import software.bernie.techarium.tile.sync.TechariumDataSlot;
import software.bernie.techarium.trait.block.BlockTraits;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;
import software.bernie.techarium.trait.behaviour.IHasBehaviour;
import software.bernie.techarium.trait.tile.TileBehaviour;
import software.bernie.techarium.trait.tile.TileTraits;

import javax.annotation.Nullable;

public abstract class MachineTileBase extends TileEntity implements ITickableTileEntity, IHasBehaviour {
    private final TileBehaviour behaviour;
    private final Map<Side, FaceConfig> sideFaceConfigs = setFaceControl();

    @Getter
    private final List<TechariumDataSlot<?>> dataSlots = new ArrayList<>();
    private final List<UUID> lastSyncedTo = new ArrayList<>();

    public MachineTileBase(TileEntityType<?> tileEntityTypeIn, TileBehaviour behaviour) {
        super(tileEntityTypeIn);
        this.behaviour = behaviour.copy();
        behaviour.tweak(this);
    }

    protected void addDataSlot(TechariumDataSlot<?> dataSlot) {
        dataSlots.add(dataSlot);
    }

    @Override
    public void tick() {
        updateMachineTile();
    }

    protected Map<Side, FaceConfig> setFaceControl(){
        Map<Side, FaceConfig> defaultSetUp = new HashMap<>();
        for (Side side: Side.values()){
            defaultSetUp.put(side,FaceConfig.NONE);
        }
        return defaultSetUp;
    }

    public Direction getFacingDirection() {
        assert this.level != null;
        BlockState state = this.level.getBlockState(this.worldPosition);
        if (state.getBlock() instanceof TechariumBlock) {
            DirectionProperty direction = ((TechariumBlock<?>) state.getBlock()).getBehaviour().getRequired(BlockTraits.BlockRotationTrait.class).getDirectionProperty();
            return state.getValue(direction);
        }
        Techarium.LOGGER.info("Machine tile did not have a MachineBlock!");
        return Direction.NORTH;
    }

    public Map<Side, FaceConfig> getFaceConfigs() {
        return sideFaceConfigs;
    }

    public ActionResultType onTileActivated(PlayerEntity player, Hand hand) {
        return ActionResultType.PASS;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        if (behaviour.has(TileTraits.PowerTrait.class)) {
            behaviour.getRequired(TileTraits.PowerTrait.class).getEnergyStorage().deserializeNBT(nbt.getCompound("energy"));
        }
        super.load(state, nbt);
        updateMachineTile();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT listNBT = new ListNBT();
        for (int i = 0; i < dataSlots.size(); i++) {
            TechariumDataSlot<?> dataSlot = dataSlots.get(i);
            if (dataSlot.getMode() == TechariumDataSlot.SyncMode.GUI)
                continue;
            Optional<CompoundNBT> optionalNBT = dataSlot.toOptionalNBT();
            if (optionalNBT.isPresent()) {
                CompoundNBT data = optionalNBT.get();
                data.putInt("dataSlotIndex", i);
                listNBT.add(data);
                dataSlots.get(i).updatePrevValue();
            }
        }
        nbt.put("data", listNBT);
        nbt.putInt("x", this.worldPosition.getX());
        nbt.putInt("y", this.worldPosition.getY());
        nbt.putInt("z", this.worldPosition.getZ());
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, nbt);
    }

    public SUpdateTileEntityPacket getFullUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT listNBT = new ListNBT();
        for (int i = 0; i < dataSlots.size(); i++) {
            TechariumDataSlot<?> dataSlot = dataSlots.get(i);
            if (dataSlot.getMode() == TechariumDataSlot.SyncMode.GUI)
                continue;
            CompoundNBT data = dataSlot.toNBT();
            data.putInt("dataSlotIndex", i);
            listNBT.add(data);
            dataSlots.get(i).updatePrevValue();
        }
        nbt.put("data", listNBT);
        nbt.putInt("x", this.worldPosition.getX());
        nbt.putInt("y", this.worldPosition.getY());
        nbt.putInt("z", this.worldPosition.getZ());
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        ListNBT listNBT = (ListNBT)nbt.get("data");
        for (INBT inbt : listNBT) {
            CompoundNBT dataNBT = (CompoundNBT) inbt;
            int dataSlotIndex = dataNBT.getInt("dataSlotIndex");
            dataSlots.get(dataSlotIndex).fromNBT(dataNBT);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        if (behaviour.has(TileTraits.PowerTrait.class)) {
            compound.put("energy", behaviour.getRequired(TileTraits.PowerTrait.class).getEnergyStorage().serializeNBT());
        }
        return super.save(compound);
    }

    protected void updateMachineTile() {
        requestModelDataUpdate();
        this.setChanged();
        boolean hasChanged = false;
        if (level == null || level.isClientSide)
            return;
        for (TechariumDataSlot<?> slot: dataSlots) {
            if (slot.needsUpdate() && slot.getMode() == TechariumDataSlot.SyncMode.RENDER) {
                hasChanged = true;
                break;
            }
        }

        Chunk chunk = level.getChunkAt(worldPosition);
        if (hasChanged) {
            PacketDistributor.TRACKING_CHUNK.with(() -> chunk).send(getUpdatePacket());
        }
        SUpdateTileEntityPacket fullPacket = getFullUpdatePacket();
        getTrackingPlayers().filter(p -> !lastSyncedTo.contains(p.getUUID())).forEach(p -> p.connection.send(fullPacket));
        lastSyncedTo.clear();
        getTrackingPlayers().forEach(p -> lastSyncedTo.add(p.getUUID()));
    }

    private Stream<ServerPlayerEntity> getTrackingPlayers() {
        Chunk chunk = level.getChunkAt(worldPosition);
        return ((ServerChunkProvider)level.getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        if (behaviour.has(TileTraits.PowerTrait.class)) {
            behaviour.getRequired(TileTraits.PowerTrait.class).getLazyEnergyStorage().invalidate();
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == CapabilityEnergy.ENERGY && behaviour.has(TileTraits.PowerTrait.class)) {
            return behaviour.getRequired(TileTraits.PowerTrait.class).getLazyEnergyStorage().cast();
        }
        return super.getCapability(cap);
    }

    public Optional<TileTraits.PowerTrait> getPowerTrait() {
        return behaviour.get(TileTraits.PowerTrait.class);
    }

    @Override
    public TileBehaviour getBehaviour() {
        return behaviour;
    }
}
