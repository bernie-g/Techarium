package software.bernie.techarium.util.loot;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import software.bernie.techarium.registry.LootRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemListLootEntry extends StandaloneLootEntry {
   private final List<Item> items;

   private ItemListLootEntry(List<Item> p_i51255_1_, int p_i51255_2_, int p_i51255_3_, ILootCondition[] p_i51255_4_, ILootFunction[] p_i51255_5_) {
      super(p_i51255_2_, p_i51255_3_, p_i51255_4_, p_i51255_5_);
      this.items = p_i51255_1_;
   }

   public LootPoolEntryType getType() {
      return LootRegistry.ITEM_LIST;
   }

   public void createItemStack(Consumer<ItemStack> p_216154_1_, LootContext p_216154_2_) {
      this.items.forEach(item -> p_216154_1_.accept(new ItemStack(item)));
   }

   public static StandaloneLootEntry.Builder<?> lootTableItemList(IItemProvider... p_216168_0_) {
      return simpleBuilder((p_216169_1_, p_216169_2_, p_216169_3_, p_216169_4_) -> {
         return new ItemListLootEntry(Arrays.stream(p_216168_0_).map(IItemProvider::asItem).collect(Collectors.toList()), p_216169_1_, p_216169_2_, p_216169_3_, p_216169_4_);
      });
   }

   public static StandaloneLootEntry.Builder<?> lootTableItemList(List<IItemProvider> p_216168_0_) {
      return simpleBuilder((p_216169_1_, p_216169_2_, p_216169_3_, p_216169_4_) -> {
         return new ItemListLootEntry(p_216168_0_.stream().map(IItemProvider::asItem).collect(Collectors.toList()), p_216169_1_, p_216169_2_, p_216169_3_, p_216169_4_);
      });
   }

   public static class Serializer extends StandaloneLootEntry.Serializer<ItemListLootEntry> {
      public void serializeCustom(JsonObject p_230422_1_, ItemListLootEntry p_230422_2_, JsonSerializationContext p_230422_3_) {
         super.serializeCustom(p_230422_1_, p_230422_2_, p_230422_3_);
         JsonArray array = new JsonArray();
         p_230422_2_.items.stream()
            .map(Registry.ITEM::getKey)
            .filter(obj -> !Objects.isNull(obj))
            .forEach(resourceLocation -> array.add(resourceLocation.toString()));

         p_230422_1_.add("names", array);
      }

      protected ItemListLootEntry deserialize(JsonObject p_212829_1_, JsonDeserializationContext p_212829_2_, int p_212829_3_, int p_212829_4_, ILootCondition[] p_212829_5_, ILootFunction[] p_212829_6_) {
         JsonArray array = p_212829_1_.getAsJsonArray("names");
         List<Item> items = new ArrayList<>();
         array.iterator().forEachRemaining(jsonElement -> items.add(JSONUtils.convertToItem(jsonElement, "it")));
         return new ItemListLootEntry(items, p_212829_3_, p_212829_4_, p_212829_5_, p_212829_6_);
      }
   }
}