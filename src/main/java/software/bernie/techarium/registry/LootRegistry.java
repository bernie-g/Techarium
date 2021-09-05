package software.bernie.techarium.registry;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import software.bernie.techarium.util.loot.ItemListLootEntry;

public class LootRegistry {
	public static LootPoolEntryType ITEM_LIST;

	private static LootPoolEntryType register(String p_237419_0_, ILootSerializer<? extends LootEntry> p_237419_1_) {
		return Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, new ResourceLocation(p_237419_0_), new LootPoolEntryType(p_237419_1_));
	}

	public static void registerLootPoolEntryTypes(RegistryEvent.NewRegistry event) {
		ITEM_LIST = register("techarium:item_list", new ItemListLootEntry.Serializer());
	}
}
