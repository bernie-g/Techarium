package software.bernie.techarium.tile.exchangestation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.display.container.ExchangeStationContainer;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.recipe.recipe.ExchangeStationRecipe;
import software.bernie.techarium.tile.base.MachineMasterTile;

import java.util.HashMap;
import java.util.Map;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.EXCHANGE_STATION_DRAWABLE;
import static software.bernie.techarium.registry.BlockRegistry.EXCHANGE_STATION;

public class ExchangeStationTile extends MachineMasterTile<ExchangeStationRecipe> implements IAnimatable {

    public boolean isOpening = false;
    private AnimationFactory factory = new AnimationFactory(this);

    public ExchangeStationTile() {
        super(EXCHANGE_STATION.getTileEntityType());
    }

    @Override
    protected MachineController<ExchangeStationRecipe> createMachineController() {
        MachineController<ExchangeStationRecipe> controller =  new MachineController<>(this, () -> this.worldPosition, EXCHANGE_STATION_DRAWABLE);
        controller.setBackground(EXCHANGE_STATION_DRAWABLE, 193, 230);
        controller.setPlayerInvSlotsXY(getPlayerInvSlotsXY());
        controller.setPlayerHotBarSlotsXY(getPlayerHotBarSlotsXY());

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

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        return new ExchangeStationContainer(this, inv, id, getDisplayName());
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

    private Map<Integer, Pair<Integer, Integer>> getPlayerInvSlotsXY() {
        Map<Integer, Pair<Integer, Integer>> playerInvSlotsXY = new HashMap<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                playerInvSlotsXY.put(j + i * 9 + 9, Pair.of(16 + j * 18, 148 + i * 18));
            }
        }
        return playerInvSlotsXY;
    }

    private Map<Integer, Pair<Integer, Integer>> getPlayerHotBarSlotsXY() {
        Map<Integer, Pair<Integer, Integer>> playerHotbarSlotsXY = new HashMap<>();
        for (int i1 = 0; i1 < 9; ++i1) {
            playerHotbarSlotsXY.put(i1, Pair.of(16 + i1 * 18, 206));
        }
        return playerHotbarSlotsXY;
    }

}
