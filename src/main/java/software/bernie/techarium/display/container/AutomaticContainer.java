package software.bernie.techarium.display.container;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.interfaces.recipe.IMachineRecipe;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.util.Vector2i;
import software.bernie.techarium.util.inventory.ContainerUtil;

import static software.bernie.techarium.registry.ContainerRegistry.AUTO_CONTAINER;

public class AutomaticContainer extends Container {

    @Getter
    protected MachineMasterTile<?> tile;

    private final Block block;

    private final BlockPos tileLocation;


    public AutomaticContainer(MachineMasterTile<?> tile, PlayerInventory inv, int id) {
        this(AUTO_CONTAINER.get(), tile, inv, id);
    }

    public AutomaticContainer(ContainerType<?> containerType, MachineMasterTile<?> tile, PlayerInventory inv, int id) {
        super(containerType, id);
        this.tile = tile;
        this.tileLocation = tile.getBlockPos();
        block = tile.getLevel().getBlockState(tile.getBlockPos()).getBlock();
        for (Integer slot : getMachineController().getPlayerInvSlotsXY().keySet()) {
            Vector2i slotXY = getMachineController().getPlayerInvSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getX(), slotXY.getY()));
        }

        for (Integer slot : getMachineController().getPlayerHotBarSlotsXY().keySet()) {
            Vector2i slotXY = getMachineController().getPlayerHotBarSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getX(), slotXY.getY()));
        }

        getMachineController().getContainerComponents().forEach(component -> this.addSlot(component.create()));

    }

    public AutomaticContainer(int id, PlayerInventory inv, PacketBuffer packetBuffer) {
        super(AUTO_CONTAINER.get(), id);
        tileLocation = packetBuffer.readBlockPos();
        this.tile = (MachineMasterTile<?>) inv.player.level.getBlockEntity(tileLocation);
        block = null;
        for (Integer slot : getMachineController().getPlayerInvSlotsXY().keySet()) {
            Vector2i slotXY = getMachineController().getPlayerInvSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getX(), slotXY.getY()));
        }

        for (Integer slot : getMachineController().getPlayerHotBarSlotsXY().keySet()) {
            Vector2i slotXY = getMachineController().getPlayerHotBarSlotsXY().get(slot);
            this.addSlot(new Slot(inv, slot, slotXY.getX(), slotXY.getY()));
        }

        getMachineController().getContainerComponents().forEach(component -> this.addSlot(component.create()));

    }

    public MachineController<? extends IMachineRecipe> getMachineController() {
        return tile.getController();
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        return ContainerUtil.handleShiftClick(this, player, index);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        if (block != null)
            return stillValid(IWorldPosCallable.create(tile.getLevel(), tileLocation), playerIn, block);
        return playerIn.distanceToSqr((double) tileLocation.getX() + 0.5D, (double) tileLocation.getY() + 0.5D, (double) tileLocation.getZ() + 0.5D) <= 64.0D;
    }
}
