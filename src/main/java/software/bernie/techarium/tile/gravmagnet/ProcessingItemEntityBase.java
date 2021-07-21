package software.bernie.techarium.tile.gravmagnet;

import java.util.Random;

import lombok.Getter;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class ProcessingItemEntityBase {

	@Getter
	protected ItemEntity item;
	protected World level;
	protected Random rand;
	protected int timer;

	public ProcessingItemEntityBase(ItemEntity item) {
		this.item = item;
		this.level = item.level;
		this.rand = level.getRandom();
	}

	public void update() {

		int multiplicator = Math.max(1, GravMagnetTile.getMagnetsOnItem(item) / 2);
		timer += multiplicator;
		item.setExtendedLifetime();

		if (level.random.nextInt(20) == 0)
			((ServerWorld) level).sendParticles(ParticleTypes.CRIT, item.getX(), item.getY() + item.getBbHeight(),
					item.getZ(), 5, 0, 0, 0, 0.1f);

		if (timer >= getRecipeTime())
			processRecipe();
	}

	protected abstract int getRecipeTime();

	protected abstract void processRecipe();

	public boolean shouldRemove() {
		return timer >= getRecipeTime();
	}

}
