package software.bernie.techarium.registry.lang;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.BlockRegistryObjectGroup;

/**
 * LMAO i started writing this with generics and it got so ridiculous I thought it'd be funny to keep it ridiculous instead of fix the generics
 *
 * good luck :)
 */
public class LangRegistryBase {

    public static <R extends RegistryObject<T>, T extends IForgeRegistryEntry<? super T>, L extends RegistryObjectLangEntry<RegistryObject<L>, L> & IForgeRegistryEntry<? super L>> L register(R registryObject, String description) {
        return (L) new RegistryObjectLangEntry<R, T>(registryObject, description).registerSelf();
    }

    public static <G extends BlockRegistryObjectGroup<B, ?, ?>, B extends Block, L extends RegistryObjectLangEntry<RegistryObject<L>, L> & IForgeRegistryEntry<? super L>> RegistryObjectLangEntry<RegistryObject<B>, B> register(G group, String description) {
        return new RegistryObjectLangEntry<>(group.getBlockRegistryObject(), description).registerSelf();
    }

    public static <T, L extends LangEntry<T, L>> L register(T object, String description) {
        return (L) new LangEntryImpl<>(object, description).registerSelf();
    }

    public static TranslationLangEntry register(String type, String name) {
        return new TranslationLangEntry(type + "." + Techarium.ModID + "." + name).registerSelf();
    }

}
