package software.bernie.techarium.block.ladder;

import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;

public class TechariumLadderBlock extends LadderBlock{

	private final float speed;
	
	public TechariumLadderBlock(Properties p_i48371_1_, float speed) {
		super(p_i48371_1_);
		this.speed = speed;
	}
	
	@Override
	public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity) {
		Vector3d motion = entity.getDeltaMovement();
		
		if (motion.y() > 0 && !entity.isSuppressingSlidingDownLadder())	
			entity.setDeltaMovement(motion.x(), speed, motion.z);
		
		return true;
	}
}
