package software.bernie.techarium.tile.sync;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemStackDataSlot extends TechariumDataSlot<ItemStack> {
    public ItemStackDataSlot(Supplier<ItemStack> getter, Consumer<ItemStack> setter) {
        super(getter, setter);
    }

    public ItemStackDataSlot(Supplier<ItemStack> getter, Consumer<ItemStack> setter, SyncMode mode) {
        super(getter, setter, mode);
    }

    @Override
    public CompoundNBT toNBT() {
        return getter.get().serializeNBT();
    }

    @Override
    public void fromNBT(CompoundNBT nbt) {
        setter.accept(ItemStack.of(nbt));
    }

    @Override
    public void updatePrevValue() {
        prevValue = getter.get() == null ? null : getter.get().copy();
    }

    @Override
    protected boolean areEqual() {
        ItemStack newItemStack = getter.get();
        return prevValue.equals(newItemStack, false);
    }
}
