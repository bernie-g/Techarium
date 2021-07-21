package software.bernie.techarium.item.armor;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.client.ClientUtils;
import software.bernie.techarium.registry.ArmorMaterialRegistry;
import software.bernie.techarium.registry.ItemGroupRegistry;
import software.bernie.techarium.registry.LangRegistry;
import software.bernie.techarium.trait.Traits;
import software.bernie.techarium.trait.behaviour.Behaviour;
import software.bernie.techarium.trait.behaviour.IHasBehaviour;
import software.bernie.techarium.trait.item.ItemBehaviour;
import software.bernie.techarium.trait.item.ItemBehaviours;
import software.bernie.techarium.util.ArmorUtil;
import software.bernie.techarium.util.Utils;

import java.util.List;

@Mod.EventBusSubscriber(modid = Techarium.ModID)
public class SurvivalistExosuitItem extends GeoArmorItem implements IAnimatable, IHasBehaviour {

    public static final EffectInstance speedBoost = new EffectInstance(Effects.MOVEMENT_SPEED, 1000000);
    public static final EffectInstance hasteBoost = new EffectInstance(Effects.DIG_SPEED, 1000000);


    public final AnimationFactory factory = new AnimationFactory(this);
    private final ItemBehaviour behaviour;

    public SurvivalistExosuitItem(EquipmentSlotType slot) {
        super(ArmorMaterialRegistry.SURVIVALIST_EXOSUIT, slot, new Item.Properties().tab(ItemGroupRegistry.TECHARIUM));
        this.behaviour = ItemBehaviours.survivalistExosuitBehaviour;
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

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (worldIn == null)
            return;
        if (worldIn.isClientSide) {
            getBehaviour().get(Traits.DescriptionTrait.class).ifPresent(descriptionTrait -> {
                if (ClientUtils.isShift()) {
                    tooltip.add(descriptionTrait.description.get());
                } else {
                    tooltip.add(LangRegistry.machineShiftDescription.get());
                }
            });
        }
    }
}
