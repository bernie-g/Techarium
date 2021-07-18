package software.bernie.techarium.item.armor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.registry.ArmorMaterialRegistry;
import software.bernie.techarium.registry.ItemGroupRegistry;
import software.bernie.techarium.util.ArmorUtil;
import software.bernie.techarium.util.Utils;

@Mod.EventBusSubscriber(modid = Techarium.ModID)
public class SurvivalistExosuitItem extends GeoArmorItem implements IAnimatable {

    public static final EffectInstance speedBoost = new EffectInstance(Effects.MOVEMENT_SPEED, 1000000);
    public static final EffectInstance hasteBoost = new EffectInstance(Effects.DIG_SPEED, 1000000);


    public final AnimationFactory factory = new AnimationFactory(this);

    public SurvivalistExosuitItem(EquipmentSlotType slot) {
        super(ArmorMaterialRegistry.SURVIVALIST_EXOSUIT, slot, new Item.Properties().tab(ItemGroupRegistry.TECHARIUM));
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (ArmorUtil.isWearingAll(player, SurvivalistExosuitItem.class)) {
            Utils.addEffect(player, speedBoost);
            Utils.addEffect(player, hasteBoost);
        } else {
            Utils.removeEffect(player, speedBoost);
            Utils.removeEffect(player, hasteBoost);
        }
    }
}
