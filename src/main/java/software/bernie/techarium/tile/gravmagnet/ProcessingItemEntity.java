package software.bernie.techarium.tile.gravmagnet;

import lombok.Getter;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.techarium.helper.EntityHelper;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;

public class ProcessingItemEntity {

	@Getter
	private ItemEntity item;
	private World level;
	private GravMagnetRecipe recipe;
	private int timer;	

	public ProcessingItemEntity(ItemEntity item, GravMagnetRecipe recipe) {
		this.item    = item;
		this.level	 = item.level;
		this.recipe  = recipe;
	}
	
	public void update() {
		
		int multiplicator = Math.max(1, GravMagnetTile.getCurrentMagnetPower(item) / 2);
		timer += multiplicator;
		item.setExtendedLifetime();
		
		if (level.random.nextInt(20) == 0)
			((ServerWorld) level).sendParticles(ParticleTypes.CRIT, item.getX(), item.getY() + item.getBbHeight(), item.getZ(), 5, 0, 0, 0, 0.1f);
		
		if (timer == recipe.getProcessTime() * 20) {
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
	
	public boolean shouldRemove() {
		return timer > 20 * 6;
	}
	
}
