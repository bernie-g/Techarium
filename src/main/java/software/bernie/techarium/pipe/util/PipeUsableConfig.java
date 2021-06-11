package software.bernie.techarium.pipe.util;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class PipeUsableConfig implements INBTSerializable<CompoundNBT> {

    @Getter
    @Setter
    private RedstoneControlType redstoneControlType = RedstoneControlType.ALWAYS_DISABLED;

    @Getter
    @Setter
    private ItemStack filter = ItemStack.EMPTY;

    public boolean isUsable(boolean redstone) {
        return redstoneControlType.isActive.apply(redstone);
    }

    public static PipeUsableConfig of(CompoundNBT nbt) {
        PipeUsableConfig config = new PipeUsableConfig();
        config.deserializeNBT(nbt);
        return config;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("redstoneType", redstoneControlType.ordinal());
        nbt.put("filter", filter.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        redstoneControlType = RedstoneControlType.values()[nbt.getInt("redstoneType")];
        filter = ItemStack.of((CompoundNBT)nbt.get("filter"));
    }
}
