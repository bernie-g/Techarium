package software.bernie.techarium.datagen;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.Techarium;

public class TechariumTags {
    public static final class Blocks {
        public static final ITag.INamedTag<Block> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final ITag.INamedTag<Block> ORES_COPPER = forge("ores/copper");
        public static final ITag.INamedTag<Block> ORES_LEAD = forge("ores/lead");
        public static final ITag.INamedTag<Block> ORES_NICKEL = forge("ores/nickel");

        public static final ITag.INamedTag<Block> BLOCKS_ALUMINIUM = forge("blocks/aluminium");
        public static final ITag.INamedTag<Block> BLOCKS_COPPER = forge("blocks/copper");
        public static final ITag.INamedTag<Block> BLOCKS_LEAD = forge("blocks/lead");
        public static final ITag.INamedTag<Block> BLOCKS_NICKEL = forge("blocks/nickel");

        private static ITag.INamedTag<Block> forge(String path) {
            return BlockTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Block> mod(String path) {
            return BlockTags.bind(new ResourceLocation(Techarium.MOD_ID, path).toString());
        }
    }

    public static final class Items {
        public static final ITag.INamedTag<Item> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final ITag.INamedTag<Item> ORES_COPPER = forge("ores/copper");
        public static final ITag.INamedTag<Item> ORES_LEAD = forge("ores/lead");
        public static final ITag.INamedTag<Item> ORES_NICKEL = forge("ores/nickel");

        public static final ITag.INamedTag<Item> INGOTS_ALUMINIUM = forge("ingots/aluminium");
        public static final ITag.INamedTag<Item> INGOTS_COPPER = forge("ingots/copper");
        public static final ITag.INamedTag<Item> INGOTS_LEAD = forge("ingots/lead");
        public static final ITag.INamedTag<Item> INGOTS_NICKEL = forge("ingots/nickel");

        public static final ITag.INamedTag<Item> BLOCKS_ALUMINIUM = forge("blocks/aluminium");
        public static final ITag.INamedTag<Item> BLOCKS_COPPER = forge("blocks/copper");
        public static final ITag.INamedTag<Item> BLOCKS_LEAD = forge("blocks/lead");
        public static final ITag.INamedTag<Item> BLOCKS_NICKEL = forge("blocks/nickel");

        public static final ITag.INamedTag<Item> NUGGETS_ALUMINIUM = forge("nuggets/aluminium");
        public static final ITag.INamedTag<Item> NUGGETS_COPPER = forge("nuggets/copper");
        public static final ITag.INamedTag<Item> NUGGETS_LEAD = forge("nuggets/lead");
        public static final ITag.INamedTag<Item> NUGGETS_NICKEL = forge("nuggets/nickel");


        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> mod(String path) {
            return ItemTags.bind(new ResourceLocation(Techarium.MOD_ID, path).toString());
        }
    }
}
