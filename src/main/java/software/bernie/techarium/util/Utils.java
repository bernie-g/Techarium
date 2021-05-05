package software.bernie.techarium.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
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
}
