package software.bernie.techarium.datagen.base;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.block.BlockRegistryObjectGroup;
import software.bernie.techarium.block.electrochromicglass.ElectroChromicGlassBlock;
import software.bernie.techarium.registry.BlockRegistry;

public abstract class TechariumBlockStateProviderBase extends BlockStateProvider {

    public TechariumBlockModelProviderBase blockProvider;
    public TechariumItemModelProviderBase itemProvider;
    public ExistingFileHelper helper;

    public TechariumBlockStateProviderBase(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Techarium.MOD_ID, exFileHelper);
        this.blockProvider = new TechariumBlockModelProviderBase(gen, exFileHelper) {
            @Override
            protected void registerModels() {
            }
        };
        helper = exFileHelper;

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

    public void machineBoxXZAndItem(BlockRegistryObjectGroup group, ModelFile modelFile) {
        this.horizontalBlock(group.getBlock(), modelFile);
        this.simpleBlockItem(group.getBlock(), modelFile);
    }
    
    public void machineBoxXYZAndItem(BlockRegistryObjectGroup group, ModelFile modelFile) {
        this.directionalBlock(group.getBlock(), modelFile);
        this.simpleBlockItem(group.getBlock(), modelFile);
    }

    public void invisMachine(Block block) {
        this.horizontalBlock(block, models().EMPTY_MODEL.get());
    }

    public void invisBlock(Block block) {
        this.simpleBlock(block, models().EMPTY_MODEL.get());
    }

    public void simpleMachineBox(BlockRegistryObjectGroup group, ResourceLocation sideTexture) {
        this.machineBoxXZAndItem(group, models().machineBox(name(group.getBlock()), sideTexture));
    }

    public void simpleMachineBoxNoItem(BlockRegistryObjectGroup group, ResourceLocation sideTexture) {
        this.horizontalBlock(group.getBlock(), models().machineBox(name(group.getBlock()), sideTexture));
    }
    
    public void simpleMachineXYZBox(BlockRegistryObjectGroup group, ResourceLocation sideTexture) {
        this.machineBoxXYZAndItem(group, models().machineBox(name(group.getBlock()), sideTexture));
    }

    public void simpleMachineXYZBoxNoItem(BlockRegistryObjectGroup group, ResourceLocation sideTexture) {
        this.directionalBlock(group.getBlock(), models().machineBox(name(group.getBlock()), sideTexture));
    }

    public void simpleBlockAndItem(Block block){
        ModelFile model = cubeAll(block);
        this.simpleBlock(block, model);
        this.simpleBlockItem(block, model);
    }

    public void simpleBooleanBlock(Block block, Property<Boolean> prop) {
    	ModelFile modelOff = models().cubeAll(name(block) + "_off", blockTexture(block, "_off"));
    	ModelFile modelOn  = models().cubeAll(name(block) + "_on", blockTexture(block, "_on"));
    	
        getVariantBuilder(block)
        .partialState().with(prop, false).addModels(new ConfiguredModel(modelOff))
        .partialState().with(prop, true).addModels(new ConfiguredModel(modelOn));
        
        this.simpleBlockItem(block, modelOff);
    }
    
    public ResourceLocation blockTexture(Block block, String end) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath() + end);
    }
    
    public void existingBlock(Block block, ResourceLocation existingModel) {
        ModelFile model = new ModelFile.ExistingModelFile(existingModel, helper);
        this.simpleBlock(block, model);
        this.simpleBlockItem(block, model);
    }

    public void existingBlockNoItem(Block block, ResourceLocation existingModel) {
        ModelFile model = new ModelFile.ExistingModelFile(existingModel, helper);
        this.simpleBlock(block, model);
    }
}
