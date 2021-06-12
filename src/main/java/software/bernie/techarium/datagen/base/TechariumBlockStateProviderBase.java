package software.bernie.techarium.datagen.base;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.BlockRegistryObjectGroup;

public abstract class TechariumBlockStateProviderBase extends BlockStateProvider {

    public TechariumBlockModelProviderBase blockProvider;
    public TechariumItemModelProviderBase itemProvider;

    public TechariumBlockStateProviderBase(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Techarium.ModID, exFileHelper);
        this.blockProvider = new TechariumBlockModelProviderBase(gen, exFileHelper) {
            @Override
            protected void registerModels() {
            }
        };

        this.itemProvider = new TechariumItemModelProviderBase(gen, exFileHelper) {
            @Override
            protected void registerModels() {
            }
        };
    }

    @Override
    public TechariumBlockModelProviderBase models() {
        return this.blockProvider;
    }


    @Override
    public TechariumItemModelProviderBase itemModels() {
        return this.itemProvider;
    }

    protected String name(Block block) {
        return block.getRegistryName().getPath();
    }

    public void machineBoxAndItem(BlockRegistryObjectGroup group, ModelFile modelFile) {
        this.horizontalBlock(group.getBlock(), modelFile);
        this.simpleBlockItem(group.getBlock(), modelFile);
    }

    public void invisMachine(Block block) {
        this.horizontalBlock(block, models().EMPTY_MODEL.get());
    }

    public void invisBlock(Block block) {
        this.simpleBlock(block, models().EMPTY_MODEL.get());
    }

    public void simpleMachineBox(BlockRegistryObjectGroup group, ResourceLocation sideTexture) {
        this.machineBoxAndItem(group, models().machineBox(name(group.getBlock()), sideTexture));
    }

    public void simpleBlockAndItem(Block block){
        ModelFile model = cubeAll(block);
        this.simpleBlock(block, model);
        this.simpleBlockItem(block, model);
    }
}
