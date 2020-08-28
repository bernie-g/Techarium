package software.bernie.techariumbotanica.tile;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import software.bernie.techariumbotanica.registry.TileEntityRegistry;

public class TileArboratorium extends TileEntity implements ITickableTileEntity
{
	public TileArboratorium()
	{
		super(TileEntityRegistry.ARBORATORIUM_TILE.get());
	}

	@Override
	public void tick()
	{

	}
}
