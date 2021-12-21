package software.bernie.techarium.tile.assembler;

import java.util.ArrayList;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.display.container.AssemblerContainer;
import software.bernie.techarium.recipe.recipe.assembler.AssemblerRecipe;
import software.bernie.techarium.recipe.recipe.assembler.AssemblerRecipeHelper;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.ItemRegistry;
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
    
	private CraftingType craftingType = CraftingType.ASSEMBLER;
	private NonNullList<ItemStack> inventory;
	
	public AssemblerTile() {
		super(BlockRegistry.ASSEMBLER.getTileEntityType());
		inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
	}

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {  
        if (isOpening) {
            event.getController().setAnimation(
                    new AnimationBuilder().addAnimation("deploy", false).addAnimation(
                            "idle", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        }
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
		
		if (craftingType == CraftingType.ASSEMBLER) {
			int stackSize = inventory.get(boxSlotId).getCount();
			if (stackSize < min) min = stackSize;
		}
		
		if (output.getCount() == 1) return min;
		
		return min;
	}
	
	public void updateOutputSlot() {
		
		ItemStack output = ItemStack.EMPTY;
		craftingType = CraftingType.ASSEMBLER;
		output = lookForAssemblerRecipes();
		
		if (output.isEmpty())  {
			craftingType = CraftingType.CRAFTING;
			output = lookForCraftingRecipes();
		}
		
		setItem(outputSlotId, output.copy());
	}

	private ItemStack lookForCraftingRecipes() {
		
		List<ICraftingRecipe> recipes  = level.getRecipeManager().getAllRecipesFor(IRecipeType.CRAFTING);
		
		for (ICraftingRecipe recipe : recipes) {
			if (checkVanillaCraft(recipe)) return recipe.getResultItem();
		}
		
		return ItemStack.EMPTY;
	}

	private ItemStack lookForAssemblerRecipes() {
		if (getItem(boxSlotId).getItem() != BlockRegistry.BOX_BLOCK.get().asItem()) return ItemStack.EMPTY;
		List<AssemblerRecipe> recipes  = level.getRecipeManager().getAllRecipesFor(RecipeRegistry.ASSEMBLER_RECIPE_TYPE);
				
		for (AssemblerRecipe recipe : recipes) {			
			boolean isShapeless = recipe.isShapeless();			
			if (recipe.getRecipePatern().isRecipeValide(getGridSlotCopy(), isShapeless)) return recipe.getOutput();
		}
		
		return ItemStack.EMPTY;
	}

	public void shrinkGrid(int count) {
		for (int i = 0; i < 9; i++) {
			if (!inventory.get(i).isEmpty())
				inventory.get(i).shrink(count);
		}
		
		if (craftingType == CraftingType.ASSEMBLER)
			inventory.get(boxSlotId).shrink(count);
		
		updateOutputSlot();
	}
	
	private boolean checkVanillaCraft(ICraftingRecipe recipe) {
				
		if (recipe instanceof ShapelessRecipe) return AssemblerRecipeHelper.checkShapeless(getGridSlotCopy(), recipe.getIngredients());
		if (recipe instanceof ShapedRecipe) return AssemblerRecipeHelper.checkNonShapeless(getGridSlotCopy(), recipe.getIngredients(), ((ShapedRecipe) recipe).getWidth(), ((ShapedRecipe) recipe).getHeight());
		
		return false;
	}
		
	private List<ItemStack> getGridSlotCopy() {
		List<ItemStack> gridInventory = new ArrayList<>(inventory);
		gridInventory.remove(gridInventory.size()-1);
		gridInventory.remove(gridInventory.size()-1);
		return gridInventory;
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

	private enum CraftingType {
		ASSEMBLER,
		CRAFTING;
	}

}
