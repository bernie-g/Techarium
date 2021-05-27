package software.bernie.techarium.tile.pipe;

import net.minecraft.tileentity.TileEntity;
import software.bernie.techarium.registry.BlockTileRegistry;

import java.util.UUID;

public class PipeTileEntity extends TileEntity {
    public boolean isInput = false;
    public UUID network = UUID.randomUUID();
    public PipeTileEntity() {
        super(BlockTileRegistry.PIPE.getTileEntityType());
    }


}
