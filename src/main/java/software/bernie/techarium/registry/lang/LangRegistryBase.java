package software.bernie.techarium.registry.lang;

import net.minecraft.block.Block;
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

    public static TranslationLangEntry registerDescription(RegistryObject<? extends Block> block) {
        return new TranslationLangEntry("description." + Techarium.ModID + "." + block.getId().getPath()).registerSelf();
    }

}
