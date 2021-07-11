package software.bernie.techarium.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.stream.Collectors;

public class ClassMap<T> extends AbstractCollection<T> {
    private final Map<Class<?>, List<T>> byClass = Maps.newHashMap();
    private final Class<T> baseClass;
    private final List<T> allInstances = Lists.newArrayList();

    public ClassMap(Class<T> p_i45909_1_) {
        this.baseClass = p_i45909_1_;
        this.byClass.put(p_i45909_1_, this.allInstances);
    }

    public boolean add(T p_add_1_) {
        boolean flag = false;

        for(Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
            if (entry.getKey().isInstance(p_add_1_)) {
                flag |= entry.getValue().add(p_add_1_);
            }
        }

        return flag;
    }

    public boolean remove(Object p_remove_1_) {
        boolean flag = false;

        for(Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
            if (entry.getKey().isInstance(p_remove_1_)) {
                List<T> list = entry.getValue();
                flag |= list.remove(p_remove_1_);
            }
        }

        return flag;
    }

    public boolean containsKey(Class clazz) {
        if (!this.baseClass.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Don't know how to search for " + clazz);
        } else {
            return this.byClass.containsKey(clazz);
        }
    }

    public boolean contains(Object p_contains_1_) {
        return this.find(p_contains_1_.getClass()).contains(p_contains_1_);
    }

    public <S> List<S> find(Class<S> clazz) {
        if (!this.baseClass.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Don't know how to search for " + clazz);
        } else {
            List<S> list = (List<S>) this.byClass.computeIfAbsent(clazz, (p_219791_1_) -> this.allInstances.stream().filter(p_219791_1_::isInstance).collect(Collectors.toList()));
            return Collections.unmodifiableList(list);
        }
    }

    public Iterator<T> iterator() {
        return (Iterator<T>)(this.allInstances.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator(this.allInstances.iterator()));
    }

    public List<T> values() {
        return ImmutableList.copyOf(this.allInstances);
    }

    public int size() {
        return this.allInstances.size();
    }

    public <S> Optional<S> findFirst(Class<S> clazz){
        return Optional.ofNullable(find(clazz)).map(s -> (S)s.get(0));
    }
}