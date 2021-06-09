package software.bernie.techarium.registry.lang;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistryObjectLangEntry<R extends RegistryObject<T>, T extends IForgeRegistryEntry<? super T>> extends LangEntry<T, RegistryObjectLangEntry<R, T>> {

    private R registryObject;

    public RegistryObjectLangEntry(R registryObject, String description) {
        super(description);
        this.registryObject = registryObject;
    }

    @Override
    public T get() {
        return registryObject.get();
    }
}
