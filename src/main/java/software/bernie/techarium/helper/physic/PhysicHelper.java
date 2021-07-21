package software.bernie.techarium.helper.physic;

import net.minecraft.entity.Entity;

public class PhysicHelper {
	
	public static double getEntityMass(Entity entity) {
		return Math.sqrt(entity.getBbWidth() * entity.getBbHeight());
	}

}
