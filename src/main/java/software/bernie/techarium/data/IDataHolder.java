package software.bernie.techarium.data;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(of = "value")
@ToString(of = "value")
public class IDataHolder<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

}
