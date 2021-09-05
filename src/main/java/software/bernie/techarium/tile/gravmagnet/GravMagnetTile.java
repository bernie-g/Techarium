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
import net.minecraft.util.Hand;
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
import software.bernie.techarium.helper.IngredientsHelper;
import software.bernie.techarium.helper.physic.PhysicHelper;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.tile.base.MachineTileBase;
import software.bernie.techarium.tile.depot.DepotTileEntity;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;
import software.bernie.techarium.trait.tile.TileBehaviour;
import software.bernie.techarium.trait.tile.TileBehaviours;

public class GravMagnetTile extends MachineTileBase implements IAnimatable, ITickableTileEntity {

	private AnimationFactory factory = new AnimationFactory(this);

	public static String identifier = "gravMagnet_";
	private int power = 8;

	@Getter
	private boolean pull = false;

	private List<ProcessingItemEntityBase> processing = new ArrayList<>();
	private List<ProcessingItemEntityBase> toRemove = new ArrayList<>();

	public GravMagnetTile() {
		super(BlockRegistry.GRAVMAGNET.getTileEntityType(), TileBehaviours.gravMagnet);
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
	public ActionResultType onTileActivated(PlayerEntity player, Hand hand) {
		return ActionResultType.PASS;
	}

	@Override
	public void tick() {
		BlockState state = level.getBlockState(getBlockPos());
		pull = state.getValue(GravMagnetBlock.POWERED);
		Direction dir = state.getValue(BlockRegistry.GRAVMAGNET.getBlock().getDirectionProperty());
		updatePower(dir);
		interactWithEntity(dir, Mode.fromBoolean(pull));
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
	private Vector3d getMotionPower(Entity entity, Direction dir, Mode mode) {

		// calculing the distance from the center of gravity of the entity, not the feet

		double entityMass = PhysicHelper.getEntityMass(entity);
		Vector3d centerPos = BlockPosHelper.getCenter(getBlockPos());

		double dist = Math.max(EntityHelper.getDistanceFromCenter(entity, centerPos), 1) / (power);
		double distPower = Math.max(0, (Math.sqrt(power + entityMass) * 0.05f) / dist);

		int mul = mode == Mode.PULL ? -1 : 1;

		return entity.getDeltaMovement().add(dir.getStepX() * distPower * mul, dir.getStepY() * distPower * mul,
				dir.getStepZ() * distPower * mul);
	}

	// ehhh Im lazy to write lol
	private void interactWithEntity(Direction dir, Mode mode) {
		AxisAlignedBB box = getBox(dir);

		BlockPos secondMagnet = null;

		if (!level.isClientSide) {
			secondMagnet = getFacingGravMagnet(dir); // check for the closest
			if (secondMagnet != null && isPull() == ((GravMagnetTile)level.getBlockEntity(secondMagnet)).isPull())
				updateDepots(secondMagnet, mode);
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

	private void updateDepots(BlockPos secondMagnet, Mode mode) {
		Vector3d center = new Vector3d(findCenter(getBlockPos().getX(), secondMagnet.getX()),
				findCenter(getBlockPos().getY(), secondMagnet.getY()),
				findCenter(getBlockPos().getZ(), secondMagnet.getZ()));
		BlockPos pos = new BlockPos(center).below();
		for (int x = 0; x <= (isBetweenBlocks(center, Direction.Axis.X) ? 1 : 0); x++) {
			for (int y = 0; y <= (isBetweenBlocks(center, Direction.Axis.Y) ? 1 : 0); y++) {
				for (int z = 0; z <= (isBetweenBlocks(center, Direction.Axis.Z) ? 1 : 0); z++) {
					updateDepot(pos.offset(x, y,z), mode);
				}
			}
		}

	}
	private void updateDepot(BlockPos pos, Mode mode) {
		TileEntity te = level.getBlockEntity(pos);
		if (te instanceof DepotTileEntity) {
			DepotTileEntity depot = (DepotTileEntity) te;
			if (mode == Mode.PULL) {
				depot.setMagnetPull(true);
			} else {
				depot.setMagnetPush(true);
			}
		}
	}

	private static boolean isBetweenBlocks(Vector3d position, Direction.Axis axis) {
		double value = axis == Direction.Axis.X ? position.x() : axis == Direction.Axis.Y ? position.y() : position.z();
		return Math.abs(value%1) > 0.2d && Math.abs(value%1) < 0.8d;
	}
	private static float findCenter(int value1, int value2) {
		return (value1+value2)/2f;
	}
	private BlockPos getFacingGravMagnet(Direction dir) {
		for (int i = 1; i <= power * 2; i++) {
			BlockPos posOffset = getBlockPos().relative(dir, i);
			BlockState localMagnet = level.getBlockState(getBlockPos());
			BlockState state = level.getBlockState(posOffset);

			if (state == localMagnet.setValue(BlockRegistry.GRAVMAGNET.getBlock().getDirectionProperty(),
					dir.getOpposite())) {
				TileEntity te = level.getBlockEntity(posOffset);

				if (te instanceof GravMagnetTile) {
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
		return isInCenter(item.position(), secondMagnet, dir);
	}

	private boolean isInCenter(Vector3d pos, BlockPos secondMagnet, Direction dir) {
		float marge = 0.5f;

		Vector3d pos1 = BlockPosHelper.getCenter(getBlockPos());
		Vector3d pos2 = BlockPosHelper.getCenter(secondMagnet);

		double dist = pos1.distanceTo(pos2) / 2;

		Vector3d center = pos1.add(dir.getStepX() * dist, dir.getStepY() * dist, dir.getStepZ() * dist);

		AxisAlignedBB box = new AxisAlignedBB(center.x - marge, center.y - marge, center.z - marge, center.x + marge,
				center.y + marge, center.z + marge);
		return box.contains(pos);
	}

	private void tryCreateProcess(ItemEntity item, Mode mode) {
		ItemStack currentStack = item.getItem();

		List<GravMagnetRecipe> recipes = level.getRecipeManager()
				.getAllRecipesFor(RecipeRegistry.GRAVMAGNET_RECIPE_TYPE);
		for (GravMagnetRecipe recipe : recipes) {
			if (Mode.fromBoolean(recipe.isPull()) != mode)
				continue;

			ItemStack input = recipe.getInput().copy();
			if (input.sameItem(currentStack)) {
				setupItem(item);
				ProcessingPullItemEntity processItem = new ProcessingPullItemEntity(item, recipe);
				processing.add(processItem);
				return;
			}
		}

		if (currentStack.getCount() >= 9 && mode == Mode.PUSH) {
			List<ICraftingRecipe> compressRecipies = level.getRecipeManager().getAllRecipesFor(IRecipeType.CRAFTING);
			for (ICraftingRecipe recipe : compressRecipies) {
				NonNullList<Ingredient> ingredients = recipe.getIngredients();
				if (ingredients.size() < 9)
					continue;

				if (IngredientsHelper.isItemInIngredient(currentStack, ingredients.get(0)) && is3x3Craft(recipe)) {
					setupItem(item);
					ProcessCompressItemEntity processItem = new ProcessCompressItemEntity(item, recipe);
					processing.add(processItem);
					return;
				}
			}
		}
	}

	private boolean is3x3Craft(ICraftingRecipe recipe) {
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		ItemStack first = IngredientsHelper.getFirstIngredient(ingredients.get(0));

		if (first == ItemStack.EMPTY)
			return false;

		for (Ingredient ingredient : ingredients) {
			if (!IngredientsHelper.isItemInIngredient(first, ingredient))
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

	private enum Mode {
		PUSH, PULL;

		public static Mode fromBoolean(boolean inverse) {
			return inverse ? PULL : PUSH;
		}
	}
}
