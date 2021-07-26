package software.bernie.techarium.display.container;

import java.util.List;
import java.util.Objects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import software.bernie.techarium.display.container.component.OutputSlot;
import software.bernie.techarium.display.container.component.WhiteListItemSlot;
import software.bernie.techarium.helper.ItemsHelper;
import software.bernie.techarium.registry.ContainerRegistry;
import software.bernie.techarium.tile.assembler.AssemblerTile;

public class AssemblerContainer extends Container {
	
	private AssemblerTile tileEntity;
	
	public AssemblerContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
		this(windowId, getTileEntity(playerInventory, data), playerInventory);
	}

	public AssemblerContainer(int id, AssemblerTile tile, PlayerInventory inv) {
		super(ContainerRegistry.ASSEMBLER_CONTAINER.get(), id);
		initSlot(tile, inv);
		tileEntity = tile;
	}
		
	@Override
    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
    	if (quickMoveBox(playerIn, index)) return ItemStack.EMPTY;
    	else if (quickMoveOutput(playerIn, index)) return ItemStack.EMPTY;
    	
    	if (index > 35) {
    		ItemStack stack = getSlot(index).getItem();
    		if (moveItemStackTo(stack, 0, 35, false)) tileEntity.updateOutputSlot();    		
    	}
    	
        return ItemStack.EMPTY;
    }
    
    private boolean quickMoveBox(PlayerEntity playerIn, int index) {
    	ItemStack stack = getSlot(index).getItem();
    	int boxSlotId   = 36 + AssemblerTile.boxSlotId;
    	
    	if (index < 36) {
    		if (stack.getItem() == AssemblerTile.boxItem) {
    			if (getSlot(boxSlotId).getItem().isEmpty()) {
    				getSlot(boxSlotId).set(stack.copy());
    				stack.setCount(0);
    			}else if (ItemsHelper.mergeItemStacks(getSlot(boxSlotId).getItem(), stack)) {
    				stack.shrink(stack.getCount());
    				return true;
    			}
    		}
    	} else if (index == boxSlotId) {
    		if (moveItemStackTo(stack, 0, 36, false)) return true;
    	}
    	
    	return false;
    }
    
    private boolean quickMoveOutput(PlayerEntity playerIn, int index) {
    	if (index != 36 + AssemblerTile.outputSlotId) return false;
    	
    	int amountMaxCanCraft = tileEntity.maxCraftInOne(64);
    	ItemStack outputStack = getSlot(36 + AssemblerTile.outputSlotId).getItem();
    	
    	for (int i = 0; i < amountMaxCanCraft; i++) {   		
    		if (!tryMergeItemInInventory(outputStack.copy())) return false;
    		tileEntity.shrinkGrid(1);
    	}
    	    	
    	return true;
    }
    
    private boolean tryMergeItemInInventory(ItemStack outputStack) {
		for (int slotId = 0; slotId < 36; slotId++) {
			Slot slot = getSlot(slotId);
			ItemStack slotStack = slot.getItem();
			
			if (slotStack.isEmpty()) {
				slot.set(outputStack);
				return true;
			}
			
			if (ItemsHelper.mergeItemStacks(slotStack, outputStack)) return true;
		}
		
		return false;
    }
    
	protected static AssemblerTile getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		
		final TileEntity tileAtPos = playerInventory.player.level.getBlockEntity(data.readBlockPos());
		
		if (tileAtPos instanceof AssemblerTile) {
			return (AssemblerTile) tileAtPos;
		}
		
		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}
	
    private void initSlot(AssemblerTile tile, PlayerInventory inv) {
        final int SLOT_DIFFERENCE = 18;
        final int posX = 9;
        final int posY = 86;
        final int posHotbarY = 144;
        
        final int gridPosX = 31;
        final int gridPosY = 19;
		
		//player inventory	
		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(inv, k, posX + k * 18, posHotbarY));
		}
        
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inv, j + (i * 9) + 9, posX + j * SLOT_DIFFERENCE, posY + i * SLOT_DIFFERENCE));
			}
		}		
		
		//Grid slot
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(tile, j + (i * 3), gridPosX + j * SLOT_DIFFERENCE, gridPosY + i * SLOT_DIFFERENCE));
			}
		}
		
		//box slot
		this.addSlot(new WhiteListItemSlot(tile, 9, 4, 37, List.of(new ItemStack(AssemblerTile.boxItem))));
		
		//output slot 
		this.addSlot(new OutputSlot(tile, 10, 125, 37));
	}


}
