package software.bernie.techarium.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Predicate;

public class CropType extends ForgeRegistryEntry<CropType> {

    private final Predicate<ItemStack> isCropAcceptable;

    public CropType(Predicate<ItemStack> isCropAcceptable) {
        this.isCropAcceptable = isCropAcceptable;
    }

    public Predicate<ItemStack> getIsCropAcceptable() {
        return isCropAcceptable;
    }
}
