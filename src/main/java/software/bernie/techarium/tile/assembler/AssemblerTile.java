package software.bernie.techarium.tile.assembler;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.display.container.AssemblerContainer;
import software.bernie.techarium.recipe.recipe.assembler.AssemblerRecipe;
import software.bernie.techarium.recipe.recipe.assembler.AssemblerRecipeHelper;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.tile.base.MachineTileBase;

public class AssemblerTile extends MachineTileBase implements IAnimatable, INamedContainerProvider, IInventory, ISidedInventory {

	AnimationFactory factory = new AnimationFactory(this);
	
    @Getter
    @Setter
    private boolean isOpening = false;
    
    public static final int boxSlotId = 9;
    public static final int outputSlotId = 10;
    public static final int inventorySize = 11;
    
	private CraftinType craftingType = CraftinType.ASSEMBLER;
	
	private NonNullList<ItemStack> inventory;
	
	public AssemblerTile() {
		super(BlockRegistry.ASSEMBLER.getTileEntityType());
		inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
	}

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {       
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
        NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer -> {
            packetBuffer.writeBlockPos(this.getBlockPos());
            packetBuffer.writeComponent(this.getDisplayName());
        });
		return ActionResultType.SUCCESS;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
		return new AssemblerContainer(id, this, inv);
	}

	@Override
	public ITextComponent getDisplayName() {
        TranslationTextComponent component = new TranslationTextComponent(this.getBlockState().getBlock().getDescriptionId());
        component.getStyle().applyFormat(TextFormatting.BLACK);
        return component;
	}

	@Override
	public void clearContent() {
		inventory.clear();
	}

	@Override
	public int getContainerSize() {
		return inventorySize;
	}

	@Override
	public ItemStack getItem(int index) {
		if (index > inventorySize - 1) return ItemStack.EMPTY;
		return inventory.get(index);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		if (index > inventorySize - 1) 
			return ItemStack.EMPTY;
				
		ItemStack stack = inventory.get(index);
		
		ItemStack stackReturn = ItemStack.EMPTY;
		
		if (count >= stack.getCount()) {
			stackReturn = removeItemNoUpdate(index);
			
		} else {
			stack.shrink(count);
			stackReturn = new ItemStack(stack.getItem(), count);
		}
		
		if (index == outputSlotId) 
			shrinkGrid(1);
	
		return stackReturn;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		if (index > inventorySize - 1) return ItemStack.EMPTY;
		
		ItemStack stack = inventory.get(index).copy();
		inventory.set(index, ItemStack.EMPTY);
		
		updateOutputSlot();
		
		return stack;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if (index > inventorySize - 1) return;
		
		inventory.set(index, stack);
		
		if (index != outputSlotId)
			updateOutputSlot();
	}
	
	public int maxCraftInOne(int count) {
		
		ItemStack output = inventory.get(outputSlotId);
		
		if (output.isEmpty()) return 0; 
		
		int min = count;
		
		for (int i = 0; i < 9; i++) {
			if (inventory.get(i).isEmpty()) continue;
			int stackSize = inventory.get(i).getCount();
			if (stackSize < min) min = stackSize;
		}
		
		if (craftingType == CraftinType.ASSEMBLER) {
			int stackSize = inventory.get(boxSlotId).getCount();
			if (stackSize < min) min = stackSize;
		}
		
		if (output.getCount() == 1) return min;
		
		return min;
	}
	
	public void updateOutputSlot() {
		
		ItemStack output = ItemStack.EMPTY;
		
		craftingType = CraftinType.ASSEMBLER;
		output = lookForAssemblerrecipes ();
		if (output.isEmpty())  {
			craftingType = CraftinType.CRAFTING;
			output = lookForCraftingrecipes ();
		}
		
		setItem(outputSlotId, output.copy());
	}

	private ItemStack lookForCraftingrecipes () {
		List<ICraftingRecipe> recipes  = level.getRecipeManager().getAllRecipesFor(IRecipeType.CRAFTING);
		
		for (ICraftingRecipe recipe : recipes ) {
			if (checkVanillaCraft(recipe)) return recipe.getResultItem();
		}
		
		return ItemStack.EMPTY;
	}

	private ItemStack lookForAssemblerrecipes () {
		if (getItem(boxSlotId).getItem() != BlockRegistry.BOX_BLOCK.get().asItem()) return ItemStack.EMPTY;
		List<AssemblerRecipe> recipes  = level.getRecipeManager().getAllRecipesFor(RecipeRegistry.ASSEMBLER_RECIPE_TYPE);
		
		for (AssemblerRecipe recipe : recipes) {
			boolean isShapeless = recipe.isShapeless();			
			if (recipe.getRecipePatern().isRecipeValide(inventory, isShapeless)) return recipe.getOutput();
		}
		
		return ItemStack.EMPTY;
	}

	public void shrinkGrid(int count) {
		for (int i = 0; i < 9; i++) {
			if (!inventory.get(i).isEmpty())
				inventory.get(i).shrink(count);
		}
		
		if (craftingType == CraftinType.ASSEMBLER)
			inventory.get(boxSlotId).shrink(count);
		
		updateOutputSlot();
	}
	
	private boolean checkVanillaCraft(ICraftingRecipe recipe) {
				
		if (recipe instanceof ShapelessRecipe) return AssemblerRecipeHelper.checkShapeless(convertShapeless(recipe), inventory);
		if (recipe instanceof ShapedRecipe) return AssemblerRecipeHelper.checkNonShapeless(convertNonShapeless((ShapedRecipe) recipe), inventory);
		
		return false;
	}
	
	private Ingredient [] convertShapeless(ICraftingRecipe recipe) {
		Ingredient [] recipePatern = new Ingredient [9];
		
		for (int i = 0; i < 9; i++) {
			if (i < recipe.getIngredients().size())	recipePatern[i] = recipe.getIngredients().get(i);
			else recipePatern[i] = Ingredient.EMPTY;
		}
		
		return recipePatern;
	}
	
	private Ingredient [] convertNonShapeless(ShapedRecipe recipe) {
		Ingredient [] recipePatern = new Ingredient [9];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int gridPos = (i * 3) + j;
				if (!fusion(recipe, recipePatern, i, j, gridPos)) recipePatern[gridPos] = Ingredient.EMPTY;
			}
		}
		return recipePatern;
		
	}
	
	private boolean fusion(ShapedRecipe recipe, Ingredient [] recipePatern, int i, int j, int gridPos) {
		for (int ii = 0; ii < recipe.getHeight(); ii++) {
			for (int jj = 0; jj < recipe.getWidth(); jj++) {
				int recipeGridPos = (ii * recipe.getWidth()) + jj;
				
				if (i == ii && j == jj) {
					recipePatern[gridPos] = recipe.getIngredients().get(recipeGridPos);
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean stillValid(PlayerEntity player) {
		if (level.getBlockEntity(getBlockPos()) != this) return false;
		else return !(player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) > 64.0D);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		ItemStackHelper.loadAllItems(nbt, inventory);
		super.load(state, nbt);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		ItemStackHelper.saveAllItems(nbt, inventory);
		return super.save(nbt);
	}
	
	@Override
	public boolean canPlaceItemThroughFace(int arg0, ItemStack arg1, Direction arg2) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int arg0, ItemStack arg1, Direction arg2) {
		return false;
	}

	@Override
	public int[] getSlotsForFace(Direction arg0) {
		return new int [0];
	}

	private enum CraftinType {
		ASSEMBLER,
		CRAFTING;
	}

}
