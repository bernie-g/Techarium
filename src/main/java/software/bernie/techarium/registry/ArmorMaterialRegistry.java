package software.bernie.techarium.registry;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvents;
import software.bernie.techarium.material.armor.TechariumArmorMaterial;

public class ArmorMaterialRegistry {
    public static final TechariumArmorMaterial SURVIVALIST_EXOSUIT = new TechariumArmorMaterial("survivalist_exosuit", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ARMOR_EQUIP_IRON, 2.0F, 0.0F, () -> Ingredient
            .of(ItemRegistry.NICKEL_INGOT.get()));
}
