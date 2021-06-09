package software.bernie.techarium.registry.lang;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.bernie.techarium.registry.LangRegistry;

@Data
@AllArgsConstructor
public abstract class LangEntry<T, L extends LangEntry<T, L>> {
    private String key;

    public abstract T get();

    public L registerSelf() {
        LangRegistry.langEntries.add(this);
        return (L) this;
    }
}
