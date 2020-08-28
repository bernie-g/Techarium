package software.bernie.techariumbotanica.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import software.bernie.techariumbotanica.registry.BlockRegistry;
import software.bernie.techariumbotanica.registry.ContainerRegistry;

public class BotariumContainer extends MachineContainer
{
	public final int tier;

	public BotariumContainer(int tier, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player)
	{
		super(BlockRegistry.BOTARIUMS.get(tier).get(), windowId, world, pos, playerInventory, player, ContainerRegistry.BOTARIUMS.get(tier).get());
		this.tier = tier;
		TileEntity tileEntity = world.getTileEntity(pos);

		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			addSlot(new SlotItemHandler(h, 0, 64, 24));
		});
		layoutPlayerInventorySlots(9, 104);
	}
}
