package software.bernie.techarium.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.vector.Vector3i;

public class TechariumCodecs {
    public static Codec<Vector3i> VECTOR3I = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("x").forGetter(Vector3i::getX),
                    Codec.INT.fieldOf("y").forGetter(Vector3i::getY),
                    Codec.INT.fieldOf("z").forGetter(Vector3i::getZ)
            ).apply(instance, Vector3i::new)
    );
}
