package software.bernie.techarium.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import software.bernie.techarium.data.DataManager;
import software.bernie.techarium.data.DataType;
import software.bernie.techarium.data.IDataProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TechariumTileEntity extends TileEntity implements IDataProvider {
    protected DataManager dataManager = new DataManager();

    public TechariumTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public DataManager getDataManager() {
        return this.dataManager;
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
        CompoundNBT nbt = super.getUpdateTag();
        return this.serializeNBTNetwork(nbt);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.deserializeNBTNetwork(tag);
    }

    public void deserializeNBTNetwork(CompoundNBT nbt) {
        this.getDataManager().deserialize(NBTDynamicOps.INSTANCE, nbt);
    }

    public CompoundNBT serializeNBTNetwork(@Nullable CompoundNBT nbt) {
        return (CompoundNBT) this.getDataManager().serialize(NBTDynamicOps.INSTANCE, nbt == null ? new CompoundNBT() : nbt, DataType.NETWORK);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt = super.save(nbt);
        return (CompoundNBT) this.getDataManager().serialize(NBTDynamicOps.INSTANCE, nbt, DataType.DISK);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT nbt) {
        super.load(p_230337_1_, nbt);
        this.getDataManager().deserialize(NBTDynamicOps.INSTANCE, nbt);
    }

    public void updateMachineTile() {
        requestModelDataUpdate();
        this.setChanged();
        if (this.getLevel() != null) {
            this.getLevel().sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }
}
