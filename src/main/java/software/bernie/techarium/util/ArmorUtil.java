package software.bernie.techarium.util;

import com.google.common.collect.Streams;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;

public class ArmorUtil {
    public static boolean isWearingAll(PlayerEntity entity, Class<? extends ArmorItem> armorType){
        return Streams.stream(entity.getArmorSlots()).allMatch(stack -> stack.getItem().getClass().isAssignableFrom(armorType));
    }
}
