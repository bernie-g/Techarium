package software.bernie.techarium.tile.depot;

import net.minecraft.inventory.InventoryHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.helper.IngredientsHelper;
import software.bernie.techarium.machine.addon.ExposeType;
import software.bernie.techarium.machine.addon.inventory.InventoryAddon;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.interfaces.recipe.IMachineRecipe;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.recipe.recipe.GravMagnetRecipe;
import software.bernie.techarium.recipe.recipe.HammerRecipe;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.tile.base.MachineMasterTile;

import java.util.List;

public class DepotTileEntity extends MachineMasterTile<IMachineRecipe> implements IAnimatable {

    public static final int SLOTS = 3;

    @Getter
    @Setter
    private boolean isMagnetPull = true;
    @Getter
    @Setter
    private boolean isMagnetPush = true;
    private int numHammered = 0;
    private AnimationFactory factory = new AnimationFactory(this);

    public DepotTileEntity() {
        super(BlockRegistry.DEPOT.getTileEntityType());
        for (Side side : Side.values()) {
            getFaceConfigs().put(side, FaceConfig.ENABLED);
        }
    }

    @Override
    public ActionResultType onTileActivated(PlayerEntity player, Hand hand) {
        if (player.getItemInHand(hand).getItem() == ItemRegistry.HAMMER.get()) {
            if (getController().getCurrentRecipe() instanceof HammerRecipe) {
                numHammered++;
                if (numHammered == 5) {
                    numHammered = 0;
                    handleProgressFinish(getController().getCurrentRecipe());
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    protected MachineController<IMachineRecipe> createMachineController() {
        return new MachineController<>(this, this::getBlockPos, null)
                .addInventory(new InventoryAddon(this, "input", 0,0, SLOTS).
                        setAllSlotStackSizes(1).setExposeType(ExposeType.INPUT)
                        .setInsertPredicate((stack, slot) -> getInventoryByName("output").getStackInSlot(slot).isEmpty() && getController().getCurrentRecipe() == null)
                        .setOnSlotChanged((itemStack, integer) -> {numHammered = 0; forceCheckRecipe();})) //you can insert when corresponding output slot is empty
                .addInventory(new InventoryAddon(this, "output", 0,0, SLOTS).setExposeType(ExposeType.OUTPUT))
                .addProgressBar(new ProgressBarAddon(this, 0, 0, 500, "main")
                .setCanProgress(value -> {
                    IMachineRecipe recipe = getController().getCurrentRecipe();
                    return recipe != null && matchRecipe(recipe) && recipe.getType() != RecipeRegistry.HAMMER_RECIPE_TYPE;
                }).setOnProgressFull(() -> handleProgressFinish(getController().getCurrentRecipe())));
    }

    public InventoryAddon getInput() {
        return getController().getMultiInventory().getInventoryByName("input").get();
    }

    public InventoryAddon getOutput() {
        return getController().getMultiInventory().getInventoryByName("output").get();
    }

    @Override
    public void forceCheckRecipe() {
        if (getController().getCurrentRecipe() != null) {
            if (!matchRecipe(castRecipe(getController().getCurrentRecipe()))) {
                getController().resetCurrentRecipe();
            }
        } else {
            getController().setShouldCheckRecipe();
        }
        updateMachineTile();
    }

    @Override
    public boolean shouldCheckForRecipe() {
        return true;
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return (recipe.getType() == RecipeRegistry.GRAVMAGNET_RECIPE_TYPE && recipe instanceof GravMagnetRecipe) || (recipe.getType() == RecipeRegistry.HAMMER_RECIPE_TYPE && recipe instanceof HammerRecipe);
    }

    @Override
    public IMachineRecipe castRecipe(IRecipe<?> iRecipe) {
        return (IMachineRecipe) iRecipe;
    }

    @Override
    public boolean matchRecipe(IMachineRecipe toCheck) {
        if (toCheck instanceof HammerRecipe) {
            HammerRecipe castedRecipe = (HammerRecipe) toCheck;
            return castedRecipe.getInput0().test(getInput().getStackInSlot(0))
                    && castedRecipe.getInput1().test(getInput().getStackInSlot(1))
                    && castedRecipe.getInput2().test(getInput().getStackInSlot(2));
        } else if (toCheck instanceof GravMagnetRecipe) {
            GravMagnetRecipe castedRecipe = (GravMagnetRecipe) toCheck;
            if (castedRecipe.isPull() && isMagnetPull() || !castedRecipe.isPull() && isMagnetPush) {
                if (castedRecipe.getInput().getCount() > 3)
                    return false;
                for (int i = 0; i < castedRecipe.getInput().getCount(); i++) {
                    if (!IngredientsHelper.areEqualIgnoreSize(getInput().getStackInSlot(i), castedRecipe.getInput()))
                        return false;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void handleProgressFinish(IMachineRecipe currentRecipe) {
        if (currentRecipe instanceof HammerRecipe) {
            HammerRecipe hammerRecipe = (HammerRecipe) currentRecipe;
            for (int i = 0; i < SLOTS; i++) {
                getInput().setStackInSlot(i, ItemStack.EMPTY);
            }
            getOutput().setStackInSlot(0, hammerRecipe.getOutput().copy());
        } else if (currentRecipe instanceof GravMagnetRecipe) {
            GravMagnetRecipe gravMagnetRecipe = (GravMagnetRecipe) currentRecipe;
            for (int i = 0; i < SLOTS; i++) {
                getInput().setStackInSlot(i, ItemStack.EMPTY);
            }
            List<ItemStack> output = gravMagnetRecipe.getOutput().getCachedOutput();

            gravMagnetRecipe.getOutput().reloadCache();
            int i = 0;
            for (ItemStack itemStack: output) {
                for (int count = 0; count < itemStack.getCount(); count++) {
                    ItemStack toPlace = itemStack.copy();
                    toPlace.setCount(1);
                    if (i < SLOTS) {
                        getOutput().setStackInSlot(i, toPlace);
                    } else {
                        InventoryHelper.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY()+0.5, getBlockPos().getZ(), toPlace);
                    }
                    i++;
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, (event) -> { event.getController().setAnimation(new AnimationBuilder().addAnimation("item", true)); return PlayState.CONTINUE;}));
    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
