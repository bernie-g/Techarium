package software.bernie.techarium.helper;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityHelper {

	public static void spawnItemEntity(World world, ItemStack stack, Vector3d pos) {
		ItemEntity entity = new ItemEntity(world, pos.x, pos.y, pos.z);
		entity.setItem(stack);
		world.addFreshEntity(entity);
	}
	
}
