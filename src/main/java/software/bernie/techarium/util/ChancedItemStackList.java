package software.bernie.techarium.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IItemProvider;
import software.bernie.techarium.Techarium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChancedItemStackList {
    @Getter
    private final List<ChancedItemStack> stackList;
    @Getter
    private List<ItemStack> cachedOutput;

    private ChancedItemStackList(ChancedItemStack... stacks) {
        this.stackList = new ArrayList<>();
        this.cachedOutput = new ArrayList<>();
        this.stackList.addAll(Arrays.asList(stacks));
        reloadCache();
    }

    public static ChancedItemStackList of(ChancedItemStack... stacks) {
        return new ChancedItemStackList(stacks);
    }

    public static ChancedItemStackList of(ItemStack... stacks) {
        return new ChancedItemStackList(Arrays.stream(stacks)
                .map(ChancedItemStack::of)
                .toArray(ChancedItemStack[]::new));
    }

    public static ChancedItemStackList of(IItemProvider... items) {
        return new ChancedItemStackList(Arrays.stream(items)
                .map(ItemStack::new)
                .map(ChancedItemStack::of)
                .toArray(ChancedItemStack[]::new));
    }

    public JsonArray toJSON() {
        JsonArray jsonArray = new JsonArray();
        for (ChancedItemStack stack : stackList) {
            jsonArray.add(stack.toJSON());
        }
        return jsonArray;
    }

    public static ChancedItemStackList fromJSON(JsonArray jsonArray) {
        List<ChancedItemStack> list = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            list.add(ChancedItemStack.fromJSON(element.getAsJsonObject()));
        }
        return new ChancedItemStackList(list.toArray(new ChancedItemStack[0]));
    }

    public static ChancedItemStackList read(PacketBuffer buffer) {
        int length = buffer.readInt();
        List<ChancedItemStack> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(ChancedItemStack.read(buffer));
        }
        return ChancedItemStackList.of(list.toArray(new ChancedItemStack[0]));
    }

    public void write(PacketBuffer buffer) {
        buffer.writeInt(stackList.size());
        for (ChancedItemStack stack : stackList) {
            stack.write(buffer);
        }
    }

    public void reloadCache() {
        cachedOutput = new ArrayList<>();
        for (ChancedItemStack stack : stackList) {
            if (stack.roll()) {
                cachedOutput.add(stack.getStack().copy());
            }
        }
    }

    public List<ItemStack> dissolve() {
        return stackList.stream().map(ChancedItemStack::getStack).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ChancedItemStackList{" + "stackList=" + stackList + ", cachedOutput=" + cachedOutput + '}';
    }
}
