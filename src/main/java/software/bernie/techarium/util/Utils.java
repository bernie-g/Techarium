package software.bernie.techarium.util;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static IFormattableTextComponent wrapText(Component adventureComponent) {
        String serialized = GsonComponentSerializer.gson().serialize(adventureComponent);
        return ITextComponent.Serializer.getComponentFromJson(serialized);
    }

    public static List<ITextComponent> wrapText(Component... adventureComponent) {
        List<ITextComponent> components = new ArrayList<>();
        for (Component component : adventureComponent) {
            String serialized = GsonComponentSerializer.gson().serialize(component);
            components.add(ITextComponent.Serializer.getComponentFromJson(serialized));
        }
        return components;
    }

    public static Ingredient deserializeIngredient(JsonObject json, String key) {
        return Ingredient.deserialize((JSONUtils.isJsonArray(json, key) ? JSONUtils.getJsonArray(json,
                "soilIn") : JSONUtils.getJsonObject(json, key)));
    }
}
