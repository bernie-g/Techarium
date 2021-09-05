package software.bernie.techarium.datagen.base;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.util.Lazy;
import software.bernie.techarium.Techarium;

public abstract class TechariumItemModelProviderBase extends ItemModelProvider {
    public final Lazy<ModelFile> EMPTY_MODEL = Lazy.of(() -> getExistingFile(mcLoc("air")));

    public TechariumItemModelProviderBase(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Techarium.MOD_ID, existingFileHelper);
    }

    public ItemModelBuilder simpleTexture(Item item, ResourceLocation texture) {
        return singleTexture(name(item), mcLoc("item/generated"), texture);
    }

    public ItemModelBuilder simpleTexture(Item item, String texture) {
        return singleTexture(name(item), mcLoc("item/generated"), "layer0", modLoc(texture));
    }

    public ItemModelBuilder emptyModel(String name){
        return withExistingParent(name, mcLoc("air"));
    }

    public String name(Item item) {
        return item.getRegistryName().getPath();
    }
}
