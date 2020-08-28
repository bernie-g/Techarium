package software.bernie.techariumbotanica.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.techariumbotanica.block.BotariumBase;
import software.bernie.techariumbotanica.container.BotariumContainer;
import software.bernie.techariumbotanica.registry.TileEntityRegistry;

import javax.annotation.Nullable;

public class TileBotarium extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
	private Integer TIER;

	public int getTier()
	{
		if (TIER == null)
		{
			BlockState blockState = this.getBlockState();
			BotariumBase block = (BotariumBase) blockState.getBlock();
			this.TIER = block.TIER;
		}
		return TIER;
	}

	public TileBotarium()
	{
		super(TileEntityRegistry.BOTARIUM_TILE.get());
	}


	@Override
	public void tick()
	{

	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent(getType().getRegistryName().getPath());
	}

	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return new BotariumContainer(getTier(), i, this.world, this.getPos(), playerInventory, playerEntity);
	}
}
