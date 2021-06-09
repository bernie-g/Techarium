package software.bernie.techarium.registry.lang;

import net.minecraft.util.text.TranslationTextComponent;

public class TranslationLangEntry extends LangEntry<TranslationTextComponent, TranslationLangEntry> {

    private String translationName;
    private TranslationTextComponent component;

    public TranslationLangEntry(String translationName) {
        super(translationName);
        this.translationName = translationName;
        this.component = new TranslationTextComponent(translationName);
    }

    @Override
    public TranslationTextComponent get() {
        return component;
    }

    public TranslationTextComponent get(Object... args) {
        return new TranslationTextComponent(translationName, args);
    }
}
