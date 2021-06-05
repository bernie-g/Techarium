package software.bernie.techarium.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;

public abstract class BaseTileBlock<T extends TileEntity> extends RotatableBlock {

    private final Supplier<T> tileSupplier;

    public BaseTileBlock(Properties properties, Supplier<T> tileSupplier) {
        super(properties);
        this.tileSupplier = tileSupplier;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return this.tileSupplier.get();
    }

}
