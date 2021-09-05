package software.bernie.techarium.tile.voltaicpile;

import net.minecraft.block.FireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import software.bernie.techarium.block.voltaicpile.VoltaicPileBlock;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.tile.base.TechariumTileBase;
import software.bernie.techarium.trait.tile.TileBehaviours;

import static software.bernie.techarium.registry.BlockRegistry.VOLTAIC_PILE;

public class VoltaicPileTile extends TechariumTileBase {
    public VoltaicPileTile() {
        super(VOLTAIC_PILE.getTileEntityType(), TileBehaviours.voltaicPile);

        for (Side side: Side.values()) {
            getFaceConfigs().put(side, FaceConfig.PUSH_ONLY);
        }
    }

    @Override
    public ActionResultType onTileActivated(PlayerEntity player) {
        return ActionResultType.PASS;
    }

    @Override
    public void tick() {
        getPowerTrait().ifPresent(trait -> {
            // Prevent immediate explosion of the Voltaic Pile, the getDayTime is a misleading name, it also includes the night
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

    @Override
    protected void updateMachineTile() {
        getPowerTrait().ifPresent(trait -> {
            float percentStored = trait.getEnergyStorage().getPercentageFull();
            VoltaicPileBlock.Charge currentChargeProperty = getBlockState().getValue(VoltaicPileBlock.CHARGE);
            VoltaicPileBlock.Charge wantedChargeProperty = VoltaicPileBlock.Charge.FULL;

            if (percentStored < 1)
                wantedChargeProperty = VoltaicPileBlock.Charge.TWO_THIRD;
            if (percentStored <= 0.5)
                wantedChargeProperty = VoltaicPileBlock.Charge.ONE_THIRD;
            if (percentStored == 0)
                wantedChargeProperty = VoltaicPileBlock.Charge.EMPTY;

            if (wantedChargeProperty != currentChargeProperty)
              this.getLevel().setBlockAndUpdate(worldPosition, this.getBlockState().setValue(VoltaicPileBlock.CHARGE, wantedChargeProperty));
          
        });
        super.updateMachineTile();
    }

    public void explode() {
        getPowerTrait().ifPresent(trait -> {
            BlockPos pos = getBlockPos();
            float explosionPower = trait.getEnergyStorage().getEnergyStored() / 100F;
            level.destroyBlock(pos, false);
            level.explode(null, pos.getX(), pos.getY(), pos.getZ(), explosionPower, Explosion.Mode.DESTROY);
        });
    }
}
