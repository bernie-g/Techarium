package software.bernie.techarium.util;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static IFormattableTextComponent wrapText(Component adventureComponent) {
        String serialized = GsonComponentSerializer.gson().serialize(adventureComponent);
        return ITextComponent.Serializer.fromJson(serialized);
    }

    public static List<ITextComponent> wrapText(Component... adventureComponent) {
        List<ITextComponent> components = new ArrayList<>();
        for (Component component : adventureComponent) {
            String serialized = GsonComponentSerializer.gson().serialize(component);
            components.add(ITextComponent.Serializer.fromJson(serialized));
        }
        return components;
    }

    public static Ingredient deserializeIngredient(JsonObject json, String key) {
        return Ingredient.fromJson((JSONUtils.isArrayNode(json, key) ? JSONUtils.getAsJsonArray(json,
                "soilIn") : JSONUtils.getAsJsonObject(json, key)));
    }

    /**
     * Rotates a VoxelShape around the center to the specified Direction, Origin is SOUTH
     * @param toRotate
     * @param direction
     * @return the rotated VoxelShape
     */
    public static VoxelShape rotateVoxelShape(VoxelShape toRotate, Direction direction) {
        VoxelShape[] buffer = new VoxelShape[]{toRotate, VoxelShapes.empty() };
        if (direction.get2DDataValue() == -1) {
            if (direction == Direction.DOWN) {
                buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.box(minX, 1-maxZ, minY, maxX, 1 - minZ, maxY)));
            } else {
                buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.box(minX, minZ, minY, maxX, maxZ, maxY)));
            }
            return buffer[1];
        }
        for (int i = 0; i < direction.get2DDataValue() % 4; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.box(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }
        return buffer[0];
    }
}
