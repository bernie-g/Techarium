package software.bernie.techarium.block;


import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegistryObjectGroup<B extends Block, I extends Item, T extends TileEntity> implements Supplier<B> {
    private final String name;
    private final Supplier<B> blockCreator;
    private final Function<B, I> itemCreator;
    private final Supplier<T> tileSupplier;

    @Getter
    private RegistryObject<B> blockRegistryObject;
    @Getter
    private RegistryObject<I> itemRegistryObject;
    @Getter
    private RegistryObject<TileEntityType<T>> tileRegistryObject;

    public BlockRegistryObjectGroup(String name, Supplier<B> blockCreator, Function<B, I> itemCreator) {
        this(name, blockCreator, itemCreator, null);
    }

    public BlockRegistryObjectGroup(String name, Supplier<B> blockCreator, Function<B, I> itemCreator, Supplier<T> tileSupplier) {
        this.name = name;
        this.blockCreator = blockCreator;
        this.itemCreator = itemCreator;
        this.tileSupplier = tileSupplier;
    }

    @Nonnull
    public B getBlock() {
        return Objects.requireNonNull(blockRegistryObject).get();
    }

    @Nonnull
    public I getItem() {
        return Objects.requireNonNull(itemRegistryObject).get();
    }

    @Nonnull
    public TileEntityType<T> getTileEntityType() {
        return Objects.requireNonNull(tileRegistryObject).get();
    }

    public BlockRegistryObjectGroup<B, I, T> register(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
        blockRegistryObject = blockRegistry.register(name, blockCreator);
        itemRegistryObject = itemRegistry.register(name, () -> itemCreator.apply(this.getBlock()));
        return this;
    }

    public BlockRegistryObjectGroup<B, I, T> registerWithoutItem(DeferredRegister<Block> blockRegistry, DeferredRegister<TileEntityType<?>> tileRegistry) {
        blockRegistryObject = blockRegistry.register(name, blockCreator);
        tileRegistryObject = tileRegistry.register(name, () -> TileEntityType.Builder.of(tileSupplier, this.getBlock())
                .build(null));
        return this;
    }
    @SuppressWarnings("ConstantConditions")
    public BlockRegistryObjectGroup<B, I, T> register(DeferredRegister<Block> blockRegistry,
                                                      DeferredRegister<Item> itemRegistry,
                                                      DeferredRegister<TileEntityType<?>> tileEntityTypeRegistry) {
        this.register(blockRegistry, itemRegistry);
        if (tileSupplier != null) {
            tileRegistryObject = tileEntityTypeRegistry.register(name, () -> TileEntityType.Builder.of(tileSupplier, this.getBlock())
                    .build(null));
        }
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public B get() {
        return this.getBlock();
    }
}