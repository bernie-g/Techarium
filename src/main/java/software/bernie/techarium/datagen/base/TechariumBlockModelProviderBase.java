package software.bernie.techarium.datagen.base;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.util.Lazy;
import software.bernie.techarium.Techarium;

public abstract class TechariumBlockModelProviderBase extends BlockModelProvider {
    public final Lazy<ModelFile> EMPTY_MODEL = Lazy.of(() -> getExistingFile(mcLoc("air")));

    public TechariumBlockModelProviderBase(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Techarium.ModID, existingFileHelper);
    }

    public BlockModelBuilder machineBox(String name, ResourceLocation texture) {
        return this.withExistingParent(name, modLoc("block/default_box")).texture("1", texture)
                .texture("particle", texture);
    }
}
