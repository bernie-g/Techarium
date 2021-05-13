package software.bernie.techarium.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class JsonCodecUtils {
    public static JsonElement serialize(FluidStack fluidStack) {
        return FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, fluidStack).result().orElseThrow(
                () -> new IllegalStateException("Could not encode output fluidStack"));
    }

    public static JsonElement serialize(ItemStack itemStack) {
        return ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, itemStack).result().orElseThrow(
                () -> new IllegalStateException("Could not encode output itemStack"));
    }

    public static FluidStack deserializeFluidStack(JsonElement json) {
        return FluidStack.CODEC.parse(JsonOps.INSTANCE, json).result().orElseThrow(
                () -> new IllegalStateException("Could not deserialize fluidstack"));
    }

    public static ItemStack deserializeItemStack(JsonElement json) {
        return ItemStack.CODEC.parse(JsonOps.INSTANCE, json).result().orElseThrow(
                () -> new IllegalStateException("Could not deserialize itemStack"));
    }
}
