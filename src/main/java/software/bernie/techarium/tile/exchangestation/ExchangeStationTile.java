package software.bernie.techarium.tile.exchangestation;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.display.container.ExchangeStationContainer;
import software.bernie.techarium.machine.addon.inventory.InventoryAddon;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.recipe.recipe.ArboretumRecipe;
import software.bernie.techarium.recipe.recipe.ExchangeStationRecipe;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.tile.base.MachineMasterTile;
import software.bernie.techarium.util.Vector2i;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.EXCHANGE_STATION_DRAWABLE;
import static software.bernie.techarium.registry.BlockRegistry.EXCHANGE_STATION;

public class ExchangeStationTile extends MachineMasterTile<ExchangeStationRecipe> implements IAnimatable {

    @Getter
    @Setter
    private boolean isOpening = false;
    private AnimationFactory factory = new AnimationFactory(this);

    public ExchangeStationTile() {
        super(EXCHANGE_STATION.getTileEntityType());
    }

    @Override
    protected MachineController<ExchangeStationRecipe> createMachineController() {
        MachineController<ExchangeStationRecipe> controller =  new MachineController<ExchangeStationRecipe>(this, () -> this.worldPosition, EXCHANGE_STATION_DRAWABLE) {
            @Override
            public Stream<ExchangeStationRecipe> getRecipes() {
                return super.getRecipes().sorted(Comparator.comparingInt(o1 -> o1.getInput().getCount()));
            }
            @Override
            public void tick() {
                if (getMultiProgressBar() != null) {
                    this.getMultiProgressBar().attemptTickAllBars();
                }
                if (getCurrentRecipe() == null) {
                    getController().getRecipes().findFirst().ifPresent(getController()::setCurrentRecipe);
                }
            }
        };
        controller.setBackground(EXCHANGE_STATION_DRAWABLE, 193, 230);
        controller.setPlayerInvSlotsXY(getPlayerInvSlotsXY());
        controller.setPlayerHotBarSlotsXY(getPlayerHotBarSlotsXY());
        controller.addInventory(new InventoryAddon(this, "input", 100,36, 1).setOnSlotChanged(this::updateOutput));
        controller.addInventory(new InventoryAddon(this, "output", 154,36, 1).setOnSlotChanged(this::onOutputChange).setInsertPredicate((itemStack, integer) -> itemStack.isEmpty()));
        return controller;
    }

    public void updateOutput(ItemStack inserted, int slot) {
        if (getController().getCurrentRecipe() == null)
            return;
        if (getController().getCurrentRecipe().getInput().sameItem(inserted) && getController().getCurrentRecipe().getInput().getCount() <= inserted.getCount()) {
            getController().getMultiInventory().getInventoryByName("output").ifPresent( output -> {
                if (getController().getCurrentRecipe().getOutput().equals(output.getStackInSlot(0)))
                    return;
                output.setStackInSlot(0, getController().getCurrentRecipe().getOutput().copy());

            });
        } else {
            getController().getMultiInventory().getInventoryByName("output").ifPresent( output -> {
                if (output.getStackInSlot(0).isEmpty())
                    return;
                output.setStackInSlot(0, ItemStack.EMPTY);
            });
        }
    }
    private void onOutputChange(ItemStack inserted, int slot) {
        if (getController().getCurrentRecipe() == null)
            return;
        if (inserted.isEmpty()) {
            InventoryAddon input = getInventoryByName("input");
            input.getStackInSlot(0).shrink(getController().getCurrentRecipe().getInput().getCount());
            updateOutput(input.getStackInSlot(0), 0);
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
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
        return new ExchangeStationContainer(this, inv, id);
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
        return recipe.getType() == RecipeRegistry.EXCHANGE_STATION_RECIPE_TYPE && recipe instanceof ExchangeStationRecipe;
    }

    @Override
    public Class<ExchangeStationRecipe> getRecipeClass() {
        return ExchangeStationRecipe.class;
    }

    @Override
    public boolean matchRecipe(ExchangeStationRecipe currentRecipe) {
        return false;
    }

    @Override
    public void handleProgressFinish(ExchangeStationRecipe currentRecipe) {

    }

    private Map<Integer, Vector2i> getPlayerInvSlotsXY() {
        Map<Integer, Vector2i> playerInvSlotsXY = new HashMap<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                playerInvSlotsXY.put(j + i * 9 + 9, new Vector2i(16 + j * 18, 148 + i * 18));
            }
        }
        return playerInvSlotsXY;
    }

    private Map<Integer, Vector2i> getPlayerHotBarSlotsXY() {
        Map<Integer, Vector2i> playerHotbarSlotsXY = new HashMap<>();
        for (int i1 = 0; i1 < 9; ++i1) {
            playerHotbarSlotsXY.put(i1, new Vector2i(16 + i1 * 18, 206));
        }
        return playerHotbarSlotsXY;
    }

}
