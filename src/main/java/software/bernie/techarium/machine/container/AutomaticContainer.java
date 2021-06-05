package software.bernie.techarium.machine.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.util.inventory.ContainerUtil;

import static software.bernie.techarium.registry.ContainerRegistry.AUTO_CONTAINER;

public class AutomaticContainer extends Container {

    protected MachineMasterTile<?> tile;

    private PlayerInventory inv;

    private ITextComponent name;

    private final BlockPos tileLocation;

    public AutomaticContainer(MachineMasterTile<?> tile, PlayerInventory inv, int id, ITextComponent containerName) {
        super(AUTO_CONTAINER.get(), id);
        this.inv = inv;
        this.name = containerName;
        this.tile = tile;
        this.tileLocation = tile.getBlockPos();

        for (Integer slot : getMachineController().getPlayerInvSlotsXY().keySet()) {
            Pair<Integer, Integer> slotXY = getMachineController().getPlayerInvSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getKey(), slotXY.getValue()));
        }

        for (Integer slot : getMachineController().getPlayerHotBarSlotsXY().keySet()) {
            Pair<Integer, Integer> slotXY = getMachineController().getPlayerHotBarSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getKey(), slotXY.getValue()));
        }

        getMachineController().getContainerComponents().forEach(component -> this.addSlot(component.create()));

    }

    public AutomaticContainer(int id, PlayerInventory inv, PacketBuffer packetBuffer) {
        super(AUTO_CONTAINER.get(), id);
        tileLocation = packetBuffer.readBlockPos();
        this.name = packetBuffer.readComponent();
        this.tile = (MachineMasterTile<?>) inv.player.level.getBlockEntity(tileLocation);

        for (Integer slot : getMachineController().getPlayerInvSlotsXY().keySet()) {
            Pair<Integer, Integer> slotXY = getMachineController().getPlayerInvSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getKey(), slotXY.getValue()));
        }

        for (Integer slot : getMachineController().getPlayerHotBarSlotsXY().keySet()) {
            Pair<Integer, Integer> slotXY = getMachineController().getPlayerHotBarSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getKey(), slotXY.getValue()));
        }

        getMachineController().getContainerComponents().forEach(component -> this.addSlot(component.create()));

    }

    private void addPlayerInventories(){

    }

    public MachineController<?> getMachineController() {
        return tile.getController();
    }

    public ITextComponent getContainerName() {
        return name;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        return ContainerUtil.handleShiftClick(this, player, index);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return playerIn.distanceToSqr((double) tileLocation.getX() + 0.5D, (double) tileLocation.getY() + 0.5D, (double) tileLocation.getZ() + 0.5D) <= 64.0D;
    }
}
