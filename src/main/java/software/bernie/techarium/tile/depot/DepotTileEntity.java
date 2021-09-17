package software.bernie.techarium.tile.depot;

import net.minecraft.inventory.InventoryHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.config.TechariumConfig;
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
import software.bernie.techarium.tile.sync.ItemStackDataSlot;
import software.bernie.techarium.tile.sync.TechariumDataSlot;

import java.util.List;

public class DepotTileEntity extends MachineMasterTile<IMachineRecipe> implements IAnimatable {

    public static final int SLOTS = 3;

    @Getter
    @Setter
    private boolean isMagnetPull = false;
    @Getter
    @Setter
    private boolean isMagnetPush = false;
    private int numHammered = 0;
    private AnimationFactory factory = new AnimationFactory(this);

    public DepotTileEntity() {
        super(BlockRegistry.DEPOT.getTileEntityType());
        for (Side side : Side.values()) {
            getFaceConfigs().put(side, FaceConfig.ENABLED);
        }
        addDataSlot(new ItemStackDataSlot(() -> getInput().getStackInSlot(0), itemStack -> getInput().setStackInSlot(0, itemStack), TechariumDataSlot.SyncMode.RENDER));
        addDataSlot(new ItemStackDataSlot(() -> getInput().getStackInSlot(1), itemStack -> getInput().setStackInSlot(1, itemStack), TechariumDataSlot.SyncMode.RENDER));
        addDataSlot(new ItemStackDataSlot(() -> getInput().getStackInSlot(2), itemStack -> getInput().setStackInSlot(2, itemStack), TechariumDataSlot.SyncMode.RENDER));
        addDataSlot(new ItemStackDataSlot(() -> getOutput().getStackInSlot(0), itemStack -> getOutput().setStackInSlot(0, itemStack), TechariumDataSlot.SyncMode.RENDER));
        addDataSlot(new ItemStackDataSlot(() -> getOutput().getStackInSlot(1), itemStack -> getOutput().setStackInSlot(1, itemStack), TechariumDataSlot.SyncMode.RENDER));
        addDataSlot(new ItemStackDataSlot(() -> getOutput().getStackInSlot(2), itemStack -> getOutput().setStackInSlot(2, itemStack), TechariumDataSlot.SyncMode.RENDER));
    }

    @Override
    public ActionResultType onTileActivated(PlayerEntity player, Hand hand) {
        if (player.getItemInHand(hand).getItem() == ItemRegistry.HAMMER.get()) {
            if (getController().getCurrentRecipe() instanceof HammerRecipe) {
                numHammered++;
                if (numHammered >= TechariumConfig.SERVER_CONFIG.numHammerForDepotRecipe.get()) {
                    numHammered = 0;
                    handleProgressFinish(getController().getCurrentRecipe());
                }
                return ActionResultType.SUCCESS;
            }
        } else {
            ItemStack inHand = player.getItemInHand(hand);
            if (inHand.isEmpty()) {
                handleEmpty(player);
            } else {
                handleFull(inHand);
            }
        }
        return ActionResultType.PASS;
    }

    private void handleEmpty(PlayerEntity player) {
        for (int i = SLOTS - 1; i >= 0; i--) {
            ItemStack stack = getOutput().getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (!player.addItem(stack.copy())) {
                    player.drop(stack.copy(), false);
                }
                getOutput().setStackInSlot(i, ItemStack.EMPTY);
                return;
            }
        }
        for (int i = SLOTS - 1; i >= 0; i--) {
            ItemStack stack = getInput().getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (!player.addItem(stack.copy())) {
                    player.drop(stack.copy(), false);
                }
                getInput().setStackInSlot(i, ItemStack.EMPTY);
                return;
            }
        }
    }

    private void handleFull(ItemStack inHand) {
        for (int i = 0; i < SLOTS; i++) {
            ItemStack inSlot = getInput().getStackInSlot(i);
            if (inSlot.isEmpty()) {
                if (getInput().getInsertPredicate().test(inHand, i)) {
                    ItemStack toInsert = inHand.copy();
                    toInsert.setCount(1);
                    getInput().setStackInSlot(i, toInsert);
                    inHand.setCount(inHand.getCount()-1);
                    return;
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        isMagnetPush = false;
        isMagnetPull = false;
    }

    @Override
    protected MachineController<IMachineRecipe> createMachineController() {
        return new MachineController<>(this, this::getBlockPos, null)
                .addInventory(new InventoryAddon(this, "input", 0,0, SLOTS)
                        .setAllSlotStackSizes(1).setExposeType(ExposeType.INPUT)
                        .setInsertPredicate((stack, slot) -> isOutputEmpty() && getController().getCurrentRecipe() == null)
                        .setOnSlotChanged((itemStack, integer) -> {numHammered = 0; forceCheckRecipe();})) //you can insert when corresponding output slot is empty
                .addInventory(new InventoryAddon(this, "output", 0,0, SLOTS).setExposeType(ExposeType.OUTPUT))
                .addProgressBar(new ProgressBarAddon(this, 0, 0, 500, "main")
                        .setCanProgress(value -> {
                            IMachineRecipe recipe = getController().getCurrentRecipe();
                            return recipe != null && matchRecipe(recipe) && recipe.getType() != RecipeRegistry.HAMMER_RECIPE_TYPE;})
                        .setOnProgressFull(() -> handleProgressFinish(getController().getCurrentRecipe()))
                        .setOnProgressTick(() -> {
                            if (level instanceof ServerWorld) {
                                ((ServerWorld) level).sendParticles(ParticleTypes.CRIT, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 1.3f,
                                        getBlockPos().getZ() + 0.5f, 5, 0, 0, 0, 0.1f);
                            }
                        })
                );
    }

    public InventoryAddon getInput() {
        return getController().getMultiInventory().getInventoryByName("input").get();
    }

    private boolean isOutputEmpty() {
        InventoryAddon output = getOutput();
        for (int i = 0; i < output.getSlots(); i++) {
            if (!output.getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
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
    public Class<IMachineRecipe> getRecipeClass() {
        return IMachineRecipe.class;
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
