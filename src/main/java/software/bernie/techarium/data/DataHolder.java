package software.bernie.techarium.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import lombok.Getter;

import java.util.Optional;


public class DataHolder<T> extends IDataHolder<T> {
    @Getter
    private final String name;
    private final Codec<T> codec;
    @Getter
    private final DataType dataType;

    public DataHolder(String name, Codec<T> codec, DataType dataType) {
        this(null, name, codec, dataType);
    }

    public DataHolder(String name, Codec<T> codec) {
        this(null, name, codec, DataType.ALL);
    }

    public DataHolder(T defaultValue, String name, Codec<T> codec) {
        this(defaultValue, name, codec, DataType.ALL);
    }

    public DataHolder(T defaultValue, String name, Codec<T> codec, DataType dataType) {
        this.name = name;
        this.codec = codec;
        this.dataType = dataType;
        this.set(defaultValue);
    }


    public <O> void deserialize(DynamicOps<O> ops, O input) {
        Optional<O> data = ops.get(input, name).result();
        data.flatMap(o -> codec.parse(ops, o).result()).ifPresent(this::set);
    }

    public <O> O serialize(DynamicOps<O> ops, O object) {
        if(object == null) return null;
        if(this.get() == null) return object;
        Optional<O> data = codec.encodeStart(ops, this.get()).result();
        if (data.isPresent()) {
            return ops.set(object, this.name, data.get());
        }
        return object;
    }

}
