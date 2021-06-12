package software.bernie.techarium.display.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.world.World;
import software.bernie.techarium.pipe.PipePosition;
import software.bernie.techarium.pipe.util.RedstoneControlType;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.ContainerRegistry;
import software.bernie.techarium.tile.pipe.PipeTile;

import java.util.Optional;

public class PipeContainer extends Container {

    public final IWorldPosCallable worldPosCallable;
    public final PipePosition tilePos;
    public World world;

    public PipeContainer(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(id, playerInventory, IWorldPosCallable.NULL, PipePosition.createFromNBT(packetBuffer.readNbt()));
    }

    public PipeContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldCallable, PipePosition pos) {
        super(ContainerRegistry.PIPE_CONTAINER.get(), id);
        worldPosCallable = worldCallable;
        tilePos = pos;
        world = playerInventory.player.level;

        final int SLOT_DIFFERENCE = 18;
        final int posX = 16;
        final int posY = 113;
        final int posHotbarY = 171;

        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, 9 + row*9 + column, posX + column*SLOT_DIFFERENCE, posY + row*SLOT_DIFFERENCE));
            }
        }
        for(int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, posX + column*SLOT_DIFFERENCE, posHotbarY));
        }
        startTracking();
    }

    public Optional<PipeTile> getPipeTile() {
        TileEntity te = world.getBlockEntity(tilePos.getPos());
        if (te instanceof PipeTile) {
            return Optional.of((PipeTile) te);
        }
        return Optional.empty();
    }

    /**
     * This method syncs Integer Values from server to Client. The {@link #addDataSlot} method adds an {@link IntReferenceHolder} to an internal list.
     * The server repeatedly calls {@link IntReferenceHolder#get()} and checks if it's value has changed. If the value has changed a {@link net.minecraft.network.play.server.SWindowPropertyPacket} with the new value and the index of the {@link IntReferenceHolder} is send to the client and the {@link IntReferenceHolder#set(int)} method is called on the client. This way the client knows all the serverdata.
     * Even though the class is called {@link IntReferenceHolder#get()} only the short representation is synced to the client. This is no issue here, because there are not more than {@link Short#MAX_VALUE} redstone control types.
     */
    protected void startTracking() {
        Optional<PipeTile> pipeTileOptional = getPipeTile();
        pipeTileOptional.ifPresent(pipeTile ->
            {
                addDataSlot(new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return pipeTile.getConfig().getInputUsableConfig().get(tilePos.getDirection()).getRedstoneControlType().ordinal();
                    }

                    @Override
                    public void set(int value) {
                        pipeTile.getConfig().getInputUsableConfig().get(tilePos.getDirection()).setRedstoneControlType(RedstoneControlType.values()[value]);
                    }
                });
                addDataSlot(new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return pipeTile.getConfig().getOutputUsableConfig().get(tilePos.getDirection()).getRedstoneControlType().ordinal();
                    }

                    @Override
                    public void set(int value) {
                        pipeTile.getConfig().getOutputUsableConfig().get(tilePos.getDirection()).setRedstoneControlType(RedstoneControlType.values()[value]);
                    }
                });
            }
        );

    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(worldPosCallable, playerIn, BlockRegistry.PIPE.getBlock());
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }
}
