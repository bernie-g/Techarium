package software.bernie.techarium.registry.lang;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.registry.LangRegistry;

@Data
@AllArgsConstructor
public abstract class LangEntry<L extends LangEntry<L>> {
    private String key;

    public abstract ITextComponent get();

    public L registerSelf() {
        LangRegistry.langEntries.add(this);
        return (L) this;
    }
}
