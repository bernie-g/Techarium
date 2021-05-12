package software.bernie.techarium.registry;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class TagRegistry {
    public static final Tags.IOptionalNamedTag<Item> DIRT = ItemTags.createOptional(new ResourceLocation("forge:dirt"));
}
