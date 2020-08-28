package software.bernie.techariumbotanica.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.entity.IAnimatable;
import software.bernie.geckolib.event.predicate.SpecialAnimationPredicate;
import software.bernie.geckolib.example.registry.TileRegistry;
import software.bernie.geckolib.manager.AnimationManager;
import software.bernie.geckolib.tesr.SpecialAnimationController;
import software.bernie.techariumbotanica.registry.TileEntityRegistry;

public class TileWorkbench extends TileEntity implements IAnimatable
{
	public AnimationManager manager = new AnimationManager();
	public SpecialAnimationController controller = new SpecialAnimationController(this, "controller", 20, this::predicate);

	private <E extends IAnimatable> boolean predicate(SpecialAnimationPredicate<E> eSpecialAnimationPredicate)
	{
		controller.setAnimation(new AnimationBuilder().addAnimation("ControlBoard.animation.deploy", false).addAnimation("ControlBoard.animation.GlobeRotateIdle", true));
		return true;
	}

	public TileWorkbench()
	{
		super(TileEntityRegistry.WORKBENCH_TILE.get());
		manager.addAnimationController(controller);
	}

	@Override
	public AnimationManager getAnimationManager()
	{
		return manager;
	}
}
