package software.bernie.techarium.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.registry.BlockRegistry;

public class TechariumBlockTagsProvider extends BlockTagsProvider {
    public TechariumBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Techarium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(TechariumTags.Blocks.ORES_ALUMINIUM).add(BlockRegistry.ALUMINIUM_ORE.get());
        tag(TechariumTags.Blocks.ORES_COPPER).add(BlockRegistry.COPPER_ORE.get());
        tag(TechariumTags.Blocks.ORES_LEAD).add(BlockRegistry.LEAD_ORE.get());
        tag(Tags.Blocks.ORES).addTag(TechariumTags.Blocks.ORES_ALUMINIUM);
        tag(Tags.Blocks.ORES).addTag(TechariumTags.Blocks.ORES_COPPER);
        tag(Tags.Blocks.ORES).addTag(TechariumTags.Blocks.ORES_LEAD);
        tag(TechariumTags.Blocks.BLOCKS_ALUMINIUM).add(BlockRegistry.ALUMINIUM_BLOCK.get());
        tag(TechariumTags.Blocks.BLOCKS_COPPER).add(BlockRegistry.COPPER_BLOCK.get());
        tag(TechariumTags.Blocks.BLOCKS_LEAD).add(BlockRegistry.LEAD_BLOCK.get());
        tag(Tags.Blocks.STORAGE_BLOCKS).addTag(TechariumTags.Blocks.BLOCKS_ALUMINIUM);
        tag(Tags.Blocks.STORAGE_BLOCKS).addTag(TechariumTags.Blocks.BLOCKS_COPPER);
        tag(Tags.Blocks.STORAGE_BLOCKS).addTag(TechariumTags.Blocks.BLOCKS_LEAD);
    }
}
