package software.bernie.techarium.tile.exchangestation;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.recipes.recipe.ExchangeStationRecipe;
import software.bernie.techarium.tile.base.MachineMasterTile;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.BOTARIUM_DRAWABLE;
import static software.bernie.techarium.registry.BlockTileRegistry.EXCHANGE_STATION;

public class ExchangeStationTile extends MachineMasterTile<ExchangeStationRecipe> implements IAnimatable {

    public boolean isOpening = false;
    private AnimationFactory factory = new AnimationFactory(this);

    public ExchangeStationTile() {
        super(EXCHANGE_STATION.getTileEntityType());
    }

    @Override
    protected MachineController<ExchangeStationRecipe> createMachineController() {
        MachineController<ExchangeStationRecipe> controller =  new MachineController<>(this, () -> this.worldPosition, BOTARIUM_DRAWABLE);
        controller.setPowered(true);
        controller.setEnergyStorage(100000,100000, 20, 20);

        return controller;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::animationPredicate));
    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (isOpening) {
            event.getController().setAnimation(
                    new AnimationBuilder().addAnimation("deploy", false).addAnimation(
                            "idle", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void forceCheckRecipe() {

    }

    @Override
    public boolean shouldCheckForRecipe() {
        return false;
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return false;
    }

    @Override
    public ExchangeStationRecipe castRecipe(IRecipe<?> iRecipe) {
        return (ExchangeStationRecipe) iRecipe;
    }


    @Override
    public boolean matchRecipe(ExchangeStationRecipe currentRecipe) {
        return false;
    }

    @Override
    public void handleProgressFinish(ExchangeStationRecipe currentRecipe) {

    }

}
