package software.bernie.techarium.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityHelper {

	public static void spawnItemEntity(World world, ItemStack stack, Vector3d pos) {
		ItemEntity entity = new ItemEntity(world, pos.x, pos.y, pos.z);
		entity.setItem(stack);
		world.addFreshEntity(entity);
	}
	
	public static void spawnItemEntity(World world, ItemStack stack, BlockPos pos) {
		ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
		entity.setItem(stack);
		world.addFreshEntity(entity);
	}

	public static double getDistanceFromCenter(Entity entity, Vector3d pos) {
	      double d0 = entity.getX() - pos.x;
	      double d1 = entity.getY() + (entity.getBbHeight() / 2) - pos.y;
	      double d2 = entity.getZ() - pos.z;
	      return d0 * d0 + d1 * d1 + d2 * d2;
	}
	
}
