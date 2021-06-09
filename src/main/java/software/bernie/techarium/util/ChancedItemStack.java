package software.bernie.techarium.util;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.Random;

public class ChancedItemStack {
    @Getter
    @Setter
    private ItemStack stack;

    @Getter
    @Setter
    private Double chance;

    private final Random random = new Random();

    private ChancedItemStack(ItemStack stack, Double chance) {
        this.stack = stack;
        this.chance = chance;
    }

    private ChancedItemStack(ItemStack stack) {
        this.stack = stack;
        this.chance = 1.0;
    }

    public static ChancedItemStack of(ItemStack stack, Double chance) {
        return new ChancedItemStack(stack, chance);
    }

    public static ChancedItemStack of(ItemStack stack) {
        return new ChancedItemStack(stack);
    }

    public static ChancedItemStack of(Item item) {
        return new ChancedItemStack(new ItemStack(item));
    }

    public static ChancedItemStack of(Item item, int amount) {
        return new ChancedItemStack(new ItemStack(item, amount));
    }

    public static ChancedItemStack of(Item item, int amount, Double chance) {
        return new ChancedItemStack(new ItemStack(item, amount), chance);
    }

    public JsonObject toJSON() {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("item", Registry.ITEM.getKey(stack.getItem()).toString());
        jsonobject.addProperty("amount", stack.getCount());
        jsonobject.addProperty("chance", chance);
        return jsonobject;
    }

    public static ChancedItemStack fromJSON(JsonObject jsonObject) {
        Item item = Registry.ITEM.get(new ResourceLocation(jsonObject.get("item").getAsString()));
        return ChancedItemStack.of(new ItemStack(item, jsonObject.get("amount").getAsInt()), jsonObject.get("chance").getAsDouble());
    }

    public static ChancedItemStack read(PacketBuffer buffer) {
        ItemStack stack = buffer.readItem();
        Double chance = buffer.readDouble();
        return ChancedItemStack.of(stack, chance);
    }

    public void write(PacketBuffer buffer) {
        buffer.writeItemStack(this.stack, false);
        buffer.writeDouble(this.chance);
    }

    public boolean roll() {
        return random.nextDouble() <= chance;
    }

    @Override
    public String toString() {
        return "ChancedItemStack{" + "stack=" + stack + ", chance=" + chance + '}';
    }
}