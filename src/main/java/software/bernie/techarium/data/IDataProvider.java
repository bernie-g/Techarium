package software.bernie.techarium.data;

import com.mojang.serialization.Codec;

public interface IDataProvider {
    DataManager getDataManager();

    default <T> DataHolder<T> createDataHolder(T defaultValue, String name, Codec<T> codec) {
        DataHolder<T> holder = new DataHolder<>(defaultValue, name, codec);
        getDataManager().registerDataHolder(holder);
        return holder;
    }

    default <T> DataHolder<T> createDataHolder(T defaultValue, String name, Codec<T> codec, DataType dataType) {
        DataHolder<T> holder = new DataHolder<>(defaultValue, name, codec, dataType);
        getDataManager().registerDataHolder(holder);
        return holder;
    }

    default <T> DataHolder<T> createDataHolder(String name, Codec<T> codec, DataType dataType) {
        DataHolder<T> holder = new DataHolder<>(name, codec, dataType);
        getDataManager().registerDataHolder(holder);
        return holder;
    }

    default <T> DataHolder<T> createDataHolder(String name, Codec<T> codec) {
        DataHolder<T> holder = new DataHolder<>(name, codec);
        getDataManager().registerDataHolder(holder);
        return holder;
    }
}
