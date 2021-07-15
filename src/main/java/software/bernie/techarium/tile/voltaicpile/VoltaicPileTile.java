package software.bernie.techarium.tile.voltaicpile;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.tile.base.TechariumTileBase;
import software.bernie.techarium.trait.tile.TileBehaviour;
import software.bernie.techarium.trait.tile.TileBehaviours;
import software.bernie.techarium.util.TechariumEnergyStorage;

import javax.annotation.Nonnull;

import static software.bernie.techarium.registry.BlockRegistry.VOLTAIC_PILE;

public class VoltaicPileTile extends TechariumTileBase implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    public VoltaicPileTile() {
        super(VOLTAIC_PILE.getTileEntityType(), TileBehaviours.voltaicPile);
    }

    @Override
    public ActionResultType onTileActivated(PlayerEntity player) {
        return ActionResultType.PASS;
    }

    @Override
    public void tick() {
        getPowerTrait().ifPresent(trait -> {
            if (level.getDayTime() % 40 == 0 && !level.isClientSide()) {
                for (Direction d : Direction.values()) {
                    if (level.getBlockState(worldPosition.relative(d)).getBlock() instanceof FireBlock && trait.getEnergyStorage().getEnergyStored() > 0) {
                        explode();
                    }
                }
            }
        });
        super.tick();
    }

    public void explode() {
        getPowerTrait().ifPresent(trait -> {
            BlockPos pos = getBlockPos();
            float explosionPower = trait.getEnergyStorage().getEnergyStored() / 100F;
            level.destroyBlock(pos, false);
            level.explode(null, pos.getX(), pos.getY(), pos.getZ(), explosionPower, Explosion.Mode.DESTROY);
        });
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}