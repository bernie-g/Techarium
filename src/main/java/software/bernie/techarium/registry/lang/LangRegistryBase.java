package software.bernie.techarium.registry.lang;

import net.minecraftforge.fml.RegistryObject;
import software.bernie.techarium.Techarium;

/**
 * LMAO i started writing this with generics and it got so ridiculous I thought it'd be funny to keep it ridiculous instead of fix the generics
 *
 * good luck :)
 */
public class LangRegistryBase {

    public static TranslationLangEntry register(String type, String name) {
        return new TranslationLangEntry(type + "." + Techarium.ModID + "." + name).registerSelf();
    }

    public static TranslationLangEntry registerDescription(RegistryObject<?> block) {
        return registerDescription(block.getId().getPath());
    }

    public static TranslationLangEntry registerDescription(String path) {
        return new TranslationLangEntry("description." + Techarium.ModID + "." + path).registerSelf();
    }
}
