package software.bernie.techarium.tile.magneticcoils;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.block.coils.MagneticCoilBlock;
import software.bernie.techarium.block.coils.MagneticCoilType;
import software.bernie.techarium.helper.EntityHelper;
import software.bernie.techarium.item.WireItem;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.tile.base.MachineTileBase;

public class MagneticCoilTile extends MachineTileBase implements IAnimatable {
	
	AnimationFactory factory     = new AnimationFactory(this);
	
	@Setter
	@Getter
	private MagneticCoilType coilType = MagneticCoilType.TIER_NULL;
	
	public MagneticCoilTile() {
		super(BlockRegistry.MAGNETIC_COIL.getTileEntityType());
	}
	
    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
    	event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
    	return PlayState.CONTINUE;
    }

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}
	
	public void removeCoil() {
		if (!level.isClientSide())
			EntityHelper.spawnItemEntity(level, new ItemStack(getItemInCoil()), getBlockPos());
		
		coilType = MagneticCoilType.TIER_NULL;
	}
	
	public boolean isCoilEmpty() {
		return coilType == MagneticCoilType.TIER_NULL;
	}
	
	public Item getItemInCoil() {
		for (WireItem wire : WireItem.getWires()) {
			if (wire.getWireType() == coilType)
				return wire;
		}
		return Items.AIR;
	}
	
	@Override
	public CompoundNBT save(CompoundNBT coumpound) {
		coumpound.putInt("typeCoil", coilType.ordinal());
		return super.save(coumpound);
	}
	
	@Override
	public void load(BlockState p_230337_1_, CompoundNBT coumpound) {
		coilType = MagneticCoilType.values()[coumpound.getInt("typeCoil")];
		super.load(p_230337_1_, coumpound);
	}

	@Override
	public ActionResultType onTileActivated(PlayerEntity player) {
		return ActionResultType.SUCCESS;
	}
}
