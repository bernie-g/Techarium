package software.bernie.techarium.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This map uses inheritance and is very similar to ClassInheritanceMultiMap, except this is isn't a multimap
 *
 * @param <T> the type parameter
 */
public class ClassInheritanceMap<T> extends AbstractMap<Class<T>, T> {
    private final Map<Class<?>, List<T>> byClass = Maps.newHashMap();
    private final Class<T> baseClass;
    private final List<T> allInstances = Lists.newArrayList();

    public ClassInheritanceMap(Class<T> baseClass) {
        this.baseClass = baseClass;
        this.byClass.put(baseClass, this.allInstances);
    }

    /**
     * This method is slow (on purpose so the find method is fast)
     */
    public T put(T value) {
        T previous = removeDuplicates(value);

        for (Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
            if (entry.getKey().isInstance(value)) {
                entry.getValue().add(value);
            }
        }
        return previous;
    }

    private T removeDuplicates(T value) {
        Class c = value.getClass();
        while (c != null && c != this.baseClass) {
            if (_find(c).size() > 0) {
                T removed = (T) _find(c).get(0);
                removeValue(removed);
                return removed;
            }
            c = c.getSuperclass();
        }
        return null;
    }

    public T removeValue(Object key) {
        boolean flag = false;

        for (Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
            if (entry.getKey().isInstance(key)) {
                List<T> list = entry.getValue();
                flag |= list.remove(key);
            }
        }

        return flag ? (T) key : null;
    }

    @Override
    public T remove(Object key) {
        if(!(key instanceof Class)) return null;

        boolean flag = false;

        for (Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
            if (entry.getKey().isAssignableFrom((Class<?>) key)) {
                List<T> list = entry.getValue();
                flag |= list.removeIf(t -> t.getClass() != baseClass && t.getClass().isAssignableFrom((Class<?>) key));
            }
        }

        return flag ? (T) key : null;
    }

    @Override
    public void putAll(@NotNull Map<? extends Class<T>, ? extends T> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        byClass.clear();
        this.allInstances.clear();
        this.byClass.put(baseClass, this.allInstances);
    }

    @NotNull
    @Override
    public Set<Class<T>> keySet() {
        return this.allInstances.stream().map(t -> ((Class<T>) t.getClass())).collect(Collectors.toSet());
    }

    /**
     * Find list.
     *
     * @param <S>   the type parameter
     * @param clazz the clazz
     * @return the list
     */
    private <S> List<S> _find(Class<S> clazz) {
        if (!this.baseClass.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Don't know how to search for " + clazz);
        } else {
            List<S> list = (List<S>) this.byClass
                    .computeIfAbsent(clazz, (p_219791_1_) -> this.allInstances.stream().filter(p_219791_1_::isInstance)
                            .collect(Collectors.toList()));
            return Collections.unmodifiableList(list);
        }
    }

    /**
     * Values list.
     *
     * @return the list
     */
    public List<T> values() {
        return ImmutableList.copyOf(this.allInstances);
    }

    @NotNull
    @Override
    public Set<Entry<Class<T>, T>> entrySet() {
        return this.allInstances.stream().map(t -> (Entry<Class<T>, T>) new ClassMapEntry((Class<T>) t.getClass(), t))
                .collect(Collectors.toSet());
    }

    @Override
    public int size() {
        return this.allInstances.size();
    }

    @Override
    public boolean isEmpty() {
        return size() != 0;
    }

    @Override
    public boolean containsKey(Object clazz) {
        if (!(clazz instanceof Class) || !this.baseClass.isAssignableFrom((Class<?>) clazz)) {
            throw new IllegalArgumentException("Don't know how to search for " + clazz);
        } else {
            return this.byClass.containsKey(clazz);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return this._find(value.getClass()).contains(value);
    }

    @Override
    public T get(Object key) {
        if (!(key instanceof Class)) return null;
        return getOptional((Class<T>) key).orElse(null);
    }

    @Nullable
    @Override
    public T put(Class<T> key, T value) {
        return this.put(value);
    }

    public <S> Optional<S> getOptional(Class<S> clazz) {
        return Optional.ofNullable(_find(clazz)).map(s -> (S) (s.size() != 0 ? s.get(0) : null));
    }

    private final class ClassMapEntry implements Map.Entry<Class<T>, T> {
        private final Class<T> key;
        private T value;

        public ClassMapEntry(Class<T> key, T value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Class<T> getKey() {
            return key;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public T setValue(T value) {
            T old = this.value;
            this.value = value;
            return old;
        }
    }
}