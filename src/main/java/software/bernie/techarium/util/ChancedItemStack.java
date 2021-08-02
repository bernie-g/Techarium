package software.bernie.techarium.util;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.text.DecimalFormat;
import java.util.*;

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
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(jsonObject.get("item").getAsString()));
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

    public ItemStack roll() {
        int count = 0;
        for (int x = 0; x < stack.getCount(); x++) {
            count += random.nextDouble() <= chance ? 1 : 0;
        }
        ItemStack output = stack.copy();
        output.setCount(count);
        return output;
    }

    public List<ITextComponent> getDisplayPercentages() {
        List<ITextComponent> components = new ArrayList<>();
        for (int i = 0; i <= stack.getCount(); i++) {
            components.add(getDisplayText(i));
        }
        components.removeIf(Objects::isNull);
        return components;
    }

    private ITextComponent getDisplayText(int count) {
        String displayPercentage = getDisplayPercentage(count);
        if (displayPercentage.equals("0%"))
            return null;
        return new StringTextComponent(count + "x: " + getDisplayPercentage(count));
    }

    private String getDisplayPercentage(int k) {
        Locale normalLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
        DecimalFormat format = new DecimalFormat("##.##%");
        String percentage = format.format(binomialPercentage(k));
        Locale.setDefault(normalLocale);
        return percentage;
    }

    private double binomialPercentage(int k) {
        return binomialCoefficient(stack.getCount(), k) * Math.pow(chance, k) * Math.pow(1 - chance, stack.getCount() - k);
    }

    //thanks stackOverflow
    private static long binomialCoefficient(int n, int k) {
        if (k>n-k)
            k=n-k;

        long b=1;
        for (int i=1, m=n; i<=k; i++, m--)
            b=b*m/i;
        return b;
    }

    @Override
    public String toString() {
        return "ChancedItemStack{" + "stack=" + stack + ", chance=" + chance + '}';
    }
}