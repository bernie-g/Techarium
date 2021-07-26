package software.bernie.techarium.tile.gravmagnet;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.techarium.block.coils.MagneticCoilBlock;
import software.bernie.techarium.block.coils.MagneticCoilType;
import software.bernie.techarium.block.gravmagnet.GravMagnetBlock;
import software.bernie.techarium.helper.BlockPosHelper;
import software.bernie.techarium.helper.EntityHelper;
import software.bernie.techarium.helper.ItemsHelper;
import software.bernie.techarium.helper.physic.PhysicHelper;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.tile.base.MachineTileBase;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;

public class GravMagnetTile extends MachineTileBase implements IAnimatable, ITickableTileEntity {

	private AnimationFactory factory = new AnimationFactory(this);

	public static String identifier = "gravMagnet_";
	private int power = 8;

	@Getter
	private boolean pull = false;

	private List<ProcessingItemEntityBase> processing = new ArrayList<ProcessingItemEntityBase>();
	private List<ProcessingItemEntityBase> toRemove = new ArrayList<ProcessingItemEntityBase>();

	public GravMagnetTile() {
		super(BlockRegistry.GRAVMAGNET.getTileEntityType());
	}

	private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
		GeckoLibCache.getInstance().parser.setValue("variable.distance", (power - 0.66) * 16);

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
		return ActionResultType.PASS;
	}

	@Override
	public void tick() {
		BlockState state = level.getBlockState(getBlockPos());
		pull = state.getValue(GravMagnetBlock.POWERED);
		Direction dir = state.getValue(BlockRegistry.GRAVMAGNET.getBlock().getDirectionProperty());
		updatePower(dir);
		interactWithEntity(dir, MODE.fromBoolean(pull));
	}

	public void updatePower(Direction dir) {
		power = MagneticCoilType.TIER_NULL.getPower();
		BlockPos behind = getBlockPos().relative(dir.getOpposite());
		BlockState state = level.getBlockState(behind);
		if (state.getBlock() instanceof MagneticCoilBlock) {
			if (state.getValue(BlockRegistry.MAGNETIC_COIL.getBlock().getDirectionProperty()) == dir) {
				TileEntity te = level.getBlockEntity(behind);
				if (te instanceof MagneticCoilTile)
					power = ((MagneticCoilTile) te).getCoilType().getPower();
			}
		}

		for (int i = 1; i <= power; i++) {
			BlockPos offset = getBlockPos().relative(dir, i);
			state = level.getBlockState(offset);
			if (state.isCollisionShapeFullBlock(level, offset)) {
				power = i - 1;
				break;
			}
		}
	}

	// return the working zone
	private AxisAlignedBB getBox(Direction dir) {
		if (dir == Direction.UP || dir == Direction.DOWN) {
			Vector3d center1 = new Vector3d(getBlockPos().getX(), getBlockPos().getY() + 0.5, getBlockPos().getZ());
			Vector3d center2 = center1.add(1, dir.getStepY() * power, 1);

			return new AxisAlignedBB(center1, center2);
		} else {
			Direction offsetWise = dir.getClockWise();

			Vector3d center1 = new Vector3d(getBlockPos().getX() + 0.5f, getBlockPos().getY(),
					getBlockPos().getZ() + 0.5f);
			Vector3d center2 = center1.add(dir.getStepX() * power, 1, dir.getStepZ() * power);

			return new AxisAlignedBB(center1, center2).inflate(offsetWise.getStepX() * 0.5, 0,
					offsetWise.getStepZ() * 0.5);
		}
	}

	// return the acceleration for the entity (from the distance and the power)
	private Vector3d getMotionPower(Entity entity, Direction dir, MODE mode) {

		// calculing the distance from the center of gravity of the entity, not the feet

		double entityMass = PhysicHelper.getEntityMass(entity);
		Vector3d centerPos = BlockPosHelper.getCenter(getBlockPos());

		double dist = Math.max(EntityHelper.getDistanceFromCenter(entity, centerPos), 1) / (power);
		double distPower = Math.max(0, (Math.sqrt(power + entityMass) * 0.05f) / dist);

		int mul = mode == MODE.PULL ? -1 : 1;

		return entity.getDeltaMovement().add(dir.getStepX() * distPower * mul, dir.getStepY() * distPower * mul,
				dir.getStepZ() * distPower * mul);
	}

	// ehhh Im lazy to write lol
	private void interactWithEntity(Direction dir, MODE mode) {
		AxisAlignedBB box = getBox(dir);

		BlockPos secondMagnet = null;

		if (!level.isClientSide) {
			secondMagnet = getFacingGravMagnet(dir); // check for the closest
			updateProcess(secondMagnet, dir); // update runnning recipies
		}

		for (Entity entity : level.getEntitiesOfClass(Entity.class, box)) {
			entity.setDeltaMovement(getMotionPower(entity, dir, mode));

			if (!level.isClientSide) {

				if (entity instanceof ItemEntity) {
					if (secondMagnet == null) {
						resetItem((ItemEntity) entity);
						continue;
					}

					if (isItemCenter((ItemEntity) entity, secondMagnet, dir)) {
						if (!isItemAlreadyProcess((ItemEntity) entity)) {
							tryCreateProcess((ItemEntity) entity, mode);
						}
					} else
						resetItem((ItemEntity) entity);
				}
			}
		}
	}

	private BlockPos getFacingGravMagnet(Direction dir) {
		for (int i = 1; i <= power * 2; i++) {
			BlockPos posOffset = getBlockPos().relative(dir, i);
			BlockState localMagnet = level.getBlockState(getBlockPos());
			BlockState state = level.getBlockState(posOffset);

			if (state == localMagnet.setValue(BlockRegistry.GRAVMAGNET.getBlock().getDirectionProperty(),
					dir.getOpposite())) {
				TileEntity te = level.getBlockEntity(posOffset);

				if (te != null && te instanceof GravMagnetTile) {
					if (((GravMagnetTile) te).power == power) {
						return posOffset;
					}
				}
			} else if (state.isCollisionShapeFullBlock(level, posOffset)) {
				return null;
			}

		}
		return null;
	}

	private boolean isItemAlreadyProcess(ItemEntity item) {
		for (ProcessingItemEntityBase process : processing) {
			if (process.getItem().equals(item))
				return true;
		}
		return false;
	}

	private boolean isItemCenter(ItemEntity item, BlockPos secondMagnet, Direction dir) {
		float marge = 0.5f;

		Vector3d pos1 = BlockPosHelper.getCenter(getBlockPos());
		Vector3d pos2 = BlockPosHelper.getCenter(secondMagnet);

		double dist = pos1.distanceTo(pos2) / 2;

		Vector3d center = pos1.add(dir.getStepX() * dist, dir.getStepY() * dist, dir.getStepZ() * dist);

		AxisAlignedBB box = new AxisAlignedBB(center.x - marge, center.y - marge, center.z - marge, center.x + marge,
				center.y + marge, center.z + marge);

		for (ItemEntity otherItem : level.getEntitiesOfClass(ItemEntity.class, box)) {
			if (otherItem.equals(item)) {
				return true;
			}
		}

		return false;
	}

	private void tryCreateProcess(ItemEntity item, MODE mode) {
		ItemStack currentStack = item.getItem();

		List<GravMagnetRecipe> recipies = level.getRecipeManager()
				.getAllRecipesFor(RecipeRegistry.GRAVMAGNET_RECIPE_TYPE);
		for (GravMagnetRecipe recipe : recipies) {
			if (MODE.fromBoolean(recipe.isPull()) != mode)
				continue;

			ItemStack input = recipe.getInput().copy();
			if (input.sameItem(currentStack)) {
				setupItem(item);
				ProcessingPullItemEntity processItem = new ProcessingPullItemEntity(item, recipe);
				processing.add(processItem);
				return;
			}
		}

		if (currentStack.getCount() >= 9 && mode == MODE.PUSH) {
			List<ICraftingRecipe> compressRecipies = level.getRecipeManager().getAllRecipesFor(IRecipeType.CRAFTING);
			for (ICraftingRecipe recipe : compressRecipies) {
				NonNullList<Ingredient> ingredients = recipe.getIngredients();
				if (ingredients.size() < 9)
					continue;

				if (ItemsHelper.isItemInIngredient(currentStack, ingredients.get(0)) && is9x9Craft(recipe)) {
					setupItem(item);
					ProcessCompressItemEntity processItem = new ProcessCompressItemEntity(item, recipe);
					processing.add(processItem);
					return;
				}
			}
		}
	}

	private boolean is9x9Craft(ICraftingRecipe recipe) {
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		ItemStack first = ItemsHelper.getFirstIngredient(ingredients.get(0));

		if (first == ItemStack.EMPTY)
			return false;

		for (Ingredient ingredient : ingredients) {
			if (!ItemsHelper.isItemInIngredient(first, ingredient))
				return false;
		}

		return true;
	}

	private void setupItem(ItemEntity item) {
		// ===== Beurk maybe a batter way to check, but it's working fine like that xD
		// ========
		item.setCustomNameVisible(false);

		int multiplicator = getMagnetsOnItem(item) + 1;
		item.setCustomName(new StringTextComponent(identifier + multiplicator));
	}

	private void resetItem(ItemEntity item) {
		item.setCustomName(null);
		item.setExtendedLifetime();
	}

	private void updateProcess(BlockPos secondMagnet, Direction dir) {
		if (secondMagnet == null) {
			processing.clear();
			return;
		}

		toRemove.clear();

		for (ProcessingItemEntityBase process : processing) {
			process.update();
			if (process.shouldRemove() || !isItemCenter(process.getItem(), secondMagnet, dir))
				toRemove.add(process);
		}

		processing.removeAll(toRemove);
	}

	public static int getMagnetsOnItem(ItemEntity item) {
		if (item.hasCustomName()) {
			String name = item.getCustomName().getString();

			if (name.contains(identifier)) {
				return Integer.parseInt(String.valueOf(name.charAt(identifier.length())));
			}
		}
		return 0;
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putInt("power", power);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		power = compound.getInt("power");
		if (power == 0)
			power = 8;
		super.load(state, compound);
	}

	private enum MODE {
		PUSH, PULL;

		public static MODE fromBoolean(boolean inverse) {
			return inverse ? PULL : PUSH;
		}
	}
}
