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
        copy(TechariumTags.Blocks.ORES_NICKEL, TechariumTags.Items.ORES_NICKEL);
        copy(TechariumTags.Blocks.ORES_ZINC, TechariumTags.Items.ORES_ZINC);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);

        copy(TechariumTags.Blocks.BLOCKS_ALUMINIUM, TechariumTags.Items.BLOCKS_ALUMINIUM);
        copy(TechariumTags.Blocks.BLOCKS_COPPER, TechariumTags.Items.BLOCKS_COPPER);
        copy(TechariumTags.Blocks.BLOCKS_LEAD, TechariumTags.Items.BLOCKS_LEAD);
        copy(TechariumTags.Blocks.BLOCKS_NICKEL, TechariumTags.Items.BLOCKS_NICKEL);
        copy(TechariumTags.Blocks.BLOCKS_ZINC, TechariumTags.Items.BLOCKS_ZINC);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

        tag(TechariumTags.Items.INGOTS_ALUMINIUM).add(ItemRegistry.ALUMINIUM_INGOT.get());
        tag(TechariumTags.Items.INGOTS_COPPER).add(ItemRegistry.COPPER_INGOT.get());
        tag(TechariumTags.Items.INGOTS_LEAD).add(ItemRegistry.LEAD_INGOT.get());
        tag(TechariumTags.Items.INGOTS_NICKEL).add(ItemRegistry.NICKEL_INGOT.get());
        tag(TechariumTags.Items.INGOTS_ZINC).add(ItemRegistry.ZINC_INGOT.get());

        tag(TechariumTags.Items.NUGGETS_ALUMINIUM).add(ItemRegistry.ALUMINIUM_NUGGET.get());
        tag(TechariumTags.Items.NUGGETS_COPPER).add(ItemRegistry.COPPER_NUGGET.get());
        tag(TechariumTags.Items.NUGGETS_LEAD).add(ItemRegistry.LEAD_NUGGET.get());
        tag(TechariumTags.Items.NUGGETS_NICKEL).add(ItemRegistry.NICKEL_NUGGET.get());
        tag(TechariumTags.Items.NUGGETS_ZINC).add(ItemRegistry.ZINC_NUGGET.get());
    }
}
