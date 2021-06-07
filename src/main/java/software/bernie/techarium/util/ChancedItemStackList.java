package software.bernie.techarium.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChancedItemStackList {
    List<ChancedItemStack> stackList;

    private ChancedItemStackList(ChancedItemStack... stacks) {
        this.stackList.addAll(Arrays.asList(stacks));
    }

    public static ChancedItemStackList of(ChancedItemStack... stacks) {
        return new ChancedItemStackList(stacks);
    }

    public JsonArray toJSON() {
        JsonArray jsonArray = new JsonArray();
        for (ChancedItemStack stack : stackList) {
            jsonArray.add(stack.toJSON());
        }
        return jsonArray;
    }

    public ChancedItemStackList fromJSON(JsonArray jsonArray) {
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
}
