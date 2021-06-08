package software.bernie.techarium.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.BlockRegistryObjectGroup;
import software.bernie.techarium.registry.lang.TranslationLangEntry;

public abstract class TechariumLangProviderBase extends LanguageProvider {
    public TechariumLangProviderBase(DataGenerator gen) {
        super(gen, Techarium.ModID, "en_us");
    }

    public void addBlock(BlockRegistryObjectGroup<?, ?, ?> key, String name) {
        add(key.getBlock().getDescriptionId(), name);
    }

    public void addBlock(RegistryObject<Block> key, String name) {
        add(key.get().getDescriptionId(), name);
    }

    public void addItem(RegistryObject<Item> key, String name) {
        add(key.get().getDescriptionId(), name);
    }

    public void addTranslation(TranslationLangEntry text, String name) {
        add(text.get().getKey(), name);
    }

    public void add(ItemGroup group, String name){
        TranslationTextComponent displayName = (TranslationTextComponent) group.getDisplayName();
        add(displayName.getKey(), name);
    }

}
