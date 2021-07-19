package software.bernie.techarium.tile.gravmagnet;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import software.bernie.techarium.helper.EntityHelper;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;

public class ProcessingPullItemEntity extends ProcessingItemEntityBase {

	private GravMagnetRecipe recipe;
	
	public ProcessingPullItemEntity(ItemEntity item, GravMagnetRecipe recipe) {
		super(item);
		this.recipe = recipe;
	}

	@Override
	protected int getRecipeTime() {
		return recipe.getProcessTime();
	}

	@Override
	protected void processRecipe() {
		ItemStack input 	   = recipe.getInput().copy();
		ItemStack currentStack = item.getItem();

		while (input.getCount() <= currentStack.getCount()) {
			currentStack.shrink(input.getCount());
			EntityHelper.spawnItemEntity(level, recipe.getOutput1().copy(), item.position());
			EntityHelper.spawnItemEntity(level, recipe.getOutput2().copy(), item.position());
			level.playSound(null, item.blockPosition(), SoundEvents.TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 1F, 1F);
		}
	}	
}
