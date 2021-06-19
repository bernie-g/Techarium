package software.bernie.techarium.data;

import com.mojang.serialization.DynamicOps;

import java.util.HashMap;
import java.util.Map;

/**
 * This is basically DataParameters that are abstracted over everything and aren't specific to entities :)
 */
public class DataManager {
    private Map<String, DataHolder<?>> dataHolders = new HashMap<>();

    public void registerDataHolder(DataHolder<?> dataHolder) {
        if (dataHolders.containsKey(dataHolder.getName())) {
            throw new IllegalStateException("Can not register duplicate dataholder: " + dataHolder.getName());
        }
        this.dataHolders.put(dataHolder.getName(), dataHolder);
    }

    public <O> O serialize(DynamicOps<O> ops, O object, DataType dataType) {
        for (DataHolder<?> holder : dataHolders.values()) {
            if (holder.getDataType() == dataType || holder.getDataType() == DataType.ALL) {
                object = holder.serialize(ops, object);
            }
        }
        return object;
    }

    public <O> void deserialize(DynamicOps<O> ops, O input) {
        for (DataHolder<?> holder : dataHolders.values()) {
            holder.deserialize(ops, input);
        }
    }
}
