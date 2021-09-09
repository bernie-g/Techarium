package software.bernie.techarium.datagen.base;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.util.Lazy;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.BlockRegistryObjectGroup;

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

    public ItemModelBuilder machineItemGen(BlockRegistryObjectGroup obj, TechariumItemModelProviderBase.MachineDimensions dimensions) {
        ModelBuilder<ItemModelBuilder>.TransformsBuilder builder = withExistingParent(name(obj.getItem()), new ResourceLocation("techarium:item/machinebase")).transforms();
        builder.transform(ModelBuilder.Perspective.GUI)
                .translation(dimensions.translation.x(), dimensions.translation.y(), dimensions.translation.z())
                .scale(dimensions.scale, dimensions.scale, dimensions.scale)
                .rotation(30, 225, 0);
        return builder.end();
    }

    protected enum MachineDimensions {
        _1X1X1(new Vector3f(0, -4.6F, 0), 0.575F),
        _1X1X2(new Vector3f(0, -6, 0), 0.375F);

        public Vector3f translation;
        public float scale;

        MachineDimensions(Vector3f translation, float scale) {
            this.translation = translation;
            this.scale = scale;
        }
    }
}
