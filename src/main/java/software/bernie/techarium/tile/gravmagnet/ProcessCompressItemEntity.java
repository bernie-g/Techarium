package software.bernie.techarium.tile.gravmagnet;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import software.bernie.techarium.helper.EntityHelper;

public class ProcessCompressItemEntity extends ProcessingItemEntityBase {

	private ICraftingRecipe recipe;
	private static final int compressSize = 9;
	
	public ProcessCompressItemEntity(ItemEntity item, ICraftingRecipe recipe) {
		super(item);
		this.recipe = recipe;
	}

	@Override
	protected int getRecipeTime() {
		return 6 * 20;
	}

	@Override
	protected void processRecipe() {
		ItemStack output 	   = recipe.getResultItem();
		ItemStack currentStack = item.getItem();

		while (compressSize <= currentStack.getCount()) {
			currentStack.shrink(compressSize);
			EntityHelper.spawnItemEntity(level, output.copy(), item.position());
			level.playSound(null, item.blockPosition(), SoundEvents.TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 1F, 1F);
		}
	}

}
