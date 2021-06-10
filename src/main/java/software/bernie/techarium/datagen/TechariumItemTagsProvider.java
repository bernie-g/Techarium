package software.bernie.techarium.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.registry.ItemRegistry;

public class TechariumItemTagsProvider extends ItemTagsProvider {
    public TechariumItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, Techarium.ModID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        copy(TechariumTags.Blocks.ORES_ALUMINIUM, TechariumTags.Items.ORES_ALUMINIUM);
        copy(TechariumTags.Blocks.ORES_COPPER, TechariumTags.Items.ORES_COPPER);
        copy(TechariumTags.Blocks.ORES_LEAD, TechariumTags.Items.ORES_LEAD);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);

        tag(TechariumTags.Items.INGOTS_ALUMINIUM).add(ItemRegistry.ALUMINIUM_INGOT.get());
        tag(TechariumTags.Items.INGOTS_COPPER).add(ItemRegistry.COPPER_INGOT.get());
        tag(TechariumTags.Items.INGOTS_LEAD).add(ItemRegistry.LEAD_INGOT.get());
    }
}
