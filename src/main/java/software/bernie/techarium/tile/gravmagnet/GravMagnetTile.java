package software.bernie.techarium.tile.gravmagnet;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.datafix.fixes.EntityHealth;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.techarium.block.gravmagnet.GravMagnetBlock;
import software.bernie.techarium.helper.BlockPosHelper;
import software.bernie.techarium.helper.EntityHelper;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.tile.base.MachineTileBase;

public class GravMagnetTile extends MachineTileBase implements IAnimatable, ITickableTileEntity {

	private AnimationFactory factory = new AnimationFactory(this);
	  
	private int 	  power = 8;
	private boolean   pull  = false;	
	
	public GravMagnetTile() {
		super(BlockRegistry.GRAVMAGNET.getTileEntityType());
	}

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
    	GeckoLibCache.getInstance().parser.setValue("variable.distance", power);
    	
    	String animation = pull ? "pulling" : "pushing";
    	event.getController().setAnimation(new AnimationBuilder().addAnimation(animation, true));
    	
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

	@Override
	public ActionResultType onTileActivated(PlayerEntity player) {
		return ActionResultType.SUCCESS;
	}

	@Override
	public void tick() {
		BlockState state = level.getBlockState(getBlockPos());
		pull 			 = state.getValue(GravMagnetBlock.POWERED);
		Direction dir    = getFacingDirection();
		interactWithEntity(dir, MODE.fromBoolean(pull));
	}
	
	public void setPower(int value) {
		power = value;
	}

	private AxisAlignedBB getBox(Direction dir) {
		Direction offsetWise = dir.getClockWise();
		
		Vector3d center1 = new Vector3d(getBlockPos().getX() + 0.5f, getBlockPos().getY(), getBlockPos().getZ() + 0.5f);
		Vector3d center2 = center1.add(dir.getStepX() * power, dir.getStepY() + 1, dir.getStepZ() * power); 
			
		return new AxisAlignedBB(center1, center2).inflate(offsetWise.getStepX() * 0.5, 0, offsetWise.getStepZ() * 0.5);
	}
	
	private Vector3d getMotionPower(Entity entity, Direction dir, MODE mode) {
		double dist  	 = Math.max(entity.distanceToSqr(BlockPosHelper.getCenter(getBlockPos())) * power, power * power * 1.5f);
		double distPower = Math.max(0, (power / dist)); 
		int mul 		 = mode == MODE.PULL ? -1 : 1;
		
		return entity.getDeltaMovement().add(dir.getStepX() * distPower * mul, 0, dir.getStepZ() * distPower * mul);
	}
	
	private void interactWithEntity(Direction dir, MODE mode) {
		AxisAlignedBB box = getBox(dir);
		
		BlockPos secondMagnet = level.isClientSide ? null : getFacingGravMagnet(getFacingDirection());
		
		for (Entity entity : level.getEntitiesOfClass(Entity.class, box)) {
			entity.setDeltaMovement(getMotionPower(entity, dir, mode));
			
			if (secondMagnet == null) continue;
			
			if (entity instanceof ItemEntity)
				applyRecepies((ItemEntity) entity, secondMagnet, dir, mode);
		}
	}
	
	private BlockPos getFacingGravMagnet(Direction dir) {
		for (int i = 1; i <= power; i++) {
			BlockPos posOffset 		= getBlockPos().relative(dir, i);
			BlockState localMagnet 	= level.getBlockState(getBlockPos());
			BlockState state 		= level.getBlockState(posOffset);
			
			if (state == localMagnet.setValue(GravMagnetBlock.FACING, dir.getOpposite())) {
				return posOffset;
			}
		}
		return null;
	}
	
	private void applyRecepies(ItemEntity item, BlockPos secondMagnet, Direction dir, MODE mode) {	
		float marge = 0.3f;
		
		if (item.getItem().getCount() <= 0 || level.random.nextInt(20) > 0) return;
		
		Vector3d pos1 = BlockPosHelper.getCenter(getBlockPos());
		Vector3d pos2 = BlockPosHelper.getCenter(secondMagnet);
		
		double dist = pos1.distanceTo(pos2) / 2;
		
		Vector3d center = pos1.add(dir.getStepX() * dist, 0, dir.getStepZ() * dist);		
		
		AxisAlignedBB box = new AxisAlignedBB(
				center.x - marge, center.y - 0.5, center.z - marge, 
				center.x + marge, center.y + marge, center.z + marge
			);
		
		for (ItemEntity otherItem : level.getEntitiesOfClass(ItemEntity.class, box)) {
			if (otherItem.equals(item) && mode == MODE.PULL) {
				executeRecepies(item);
				break;
			}
		}
	}
	
	private void executeRecepies(ItemEntity item) {
		ItemStack currentStack = item.getItem();
		
		List<GravMagnetRecipe> recipies = level.getRecipeManager().getAllRecipesFor(RecipeRegistry.GRAVMAGNET_RECIPE_TYPE);
		for (GravMagnetRecipe recipe : recipies) {
			ItemStack input = recipe.getInput().copy();
			if (input.sameItem(currentStack) && input.getCount() <= currentStack.getCount()) {
				currentStack.shrink(input.getCount());
				EntityHelper.spawnItemEntity(level, recipe.getOutput1().copy(), item.position());
				EntityHelper.spawnItemEntity(level, recipe.getOutput2().copy(), item.position());
				level.playSound(null, item.blockPosition(), SoundEvents.TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 1F, 1F);
				return;
			}
		}
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putInt("power", power);
		return super.save(compound);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		power = compound.getInt("power");
		if (power == 0) power = 8;
		super.load(state, compound);
	}
	
	private enum MODE {
		PUSH,
		PULL;
		
		public static MODE fromBoolean(boolean inverse) {
			return inverse ? PULL : PUSH;
		}
	}
}
