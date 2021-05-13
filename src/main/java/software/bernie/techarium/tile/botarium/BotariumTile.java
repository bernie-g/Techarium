package software.bernie.techarium.tile.botarium;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.item.UpgradeItem;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.machine.addon.fluid.MultiTankCapHandler;
import software.bernie.techarium.machine.addon.inventory.DrawableInventoryAddon;
import software.bernie.techarium.machine.addon.inventory.InventoryAddon;
import software.bernie.techarium.machine.addon.inventory.MultiItemCapHandler;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.RecipeRegistry;
import software.bernie.techarium.tile.base.MultiblockMasterTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.BOTARIUM_DRAWABLE;
import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.BOTARIUM_OUTPUT_SLOT;
import static software.bernie.techarium.registry.BlockTileRegistry.BOTARIUM;
import static software.bernie.techarium.registry.BlockTileRegistry.BOTARIUM_TOP;

public class BotariumTile extends MultiblockMasterTile<BotariumRecipe> implements IAnimatable {
    private final int sizeX = 172;
    private final int sizeY = 184;

    private AnimationFactory factory = new AnimationFactory(this);

    public boolean isOpening = false;

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (isOpening) {
            event.getController().setAnimation(
                    new AnimationBuilder().addAnimation("Botarium.anim.deploy", false).addAnimation(
                            "Botarium.anim.idle", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("Botarium.anim.idle", true));
        }
        return PlayState.CONTINUE;
    }


    public BotariumTile() {
        super(BOTARIUM.getTileEntityType());
    }

    @Override
    protected MachineController<BotariumRecipe> createMachineController() {
        return machineController(BOTARIUM_DRAWABLE);
    }

    private MachineController<BotariumRecipe> machineController(IDrawable background) {
        MachineController<BotariumRecipe> controller = createController();
        controller.setBackground(background, sizeX, sizeY);
        controller.setPowered(true);
        controller.setEnergyStorage(10000, 10000, 8, 35);

        ProgressBarAddon progressBarAddon = new ProgressBarAddon(this, 8, 26, 500, "techarium.gui.mainprogress");
        controller.addProgressBar(progressBarAddon
                .setCanProgress((value) -> {
                    BotariumRecipe recipe = getController().getCurrentRecipe();
                    FluidStack fluid = controller.getMultiTank().getFluidTanks().get(0).getFluid();
                    InventoryAddon output = controller.getMultiInventory().getInventoryByName("output").get();
                    return recipe != null && output.getStackInSlot(0).getCount() + recipe.getRecipeOutput().getCount() <= output.getSlotLimit(0) && getController().getEnergyStorage().getEnergyStored() > 0 && fluid.getAmount() >= recipe.getFluidIn().getAmount();
                })
                .setOnProgressFull(() -> handleProgressFinish(getController().getCurrentRecipe()))
                .setOnProgressTick(() -> {
                    if (controller.getCurrentRecipe() != null) {
                        controller.getLazyEnergyStorage().ifPresent(iEnergyStorage -> iEnergyStorage.extractEnergy(controller.getCurrentRecipe().getRfPerTick(), false));
                    }
                })
        );

        controller.addTank(new FluidTankAddon(this, "waterIn", 10000, 23, 35,
                (fluidStack -> fluidStack.getFluid() instanceof WaterFluid)));

        controller.addInventory(new InventoryAddon(this, "soilInput", 49, 67, 1)
                .setInputFilter((itemStack, integer) -> Block.getBlockFromItem(itemStack.getItem()) != Blocks.AIR)
                .setOnSlotChanged((itemStack, integer) -> forceCheckRecipe()).setSlotStackSize(0, 1));

        controller.addInventory(new InventoryAddon(this, "cropInput", 49, 35, 1)
                .setInputFilter((itemStack, integer) -> world.getRecipeManager().getRecipes()
                        .stream()
                        .filter(this::checkRecipe)
                        .map(this::castRecipe).anyMatch(
                                recipe -> recipe.getCropType().test(itemStack))
                ).setOnSlotChanged((itemStack, integer) -> forceCheckRecipe()).setSlotStackSize(0, 1)
        );

        controller.addInventory(new InventoryAddon(this, "upgradeSlot", 83, 81, 4)
                .setInputFilter((itemStack, integer) -> itemStack.getItem() instanceof UpgradeItem));

        controller.addInventory(
                new DrawableInventoryAddon(this, "output", 183, 49, BOTARIUM_OUTPUT_SLOT, 178, 34, 30, 46, 1)
                        .setInputFilter((itemStack, integer) -> false)
                        .setOnSlotChanged((itemStack, integer) -> forceCheckRecipe()).setSlotStackSize(0, 64));

        return controller;
    }

    public InventoryAddon getCropInventory() {
        return getController().getMultiInventory().getInvOptional().map(inv -> inv).orElse(
                new MultiItemCapHandler(new ArrayList<>())).getInventories().stream().filter(
                addon -> addon.getName().contains("cropInput")).findFirst().orElseThrow(NullPointerException::new);
    }

    public InventoryAddon getOutputInventory() {
        return getController().getMultiInventory().getInvOptional().map(inv -> inv).orElse(
                new MultiItemCapHandler(new ArrayList<>())).getInventories().stream().filter(
                addon -> addon.getName().contains("output")).findFirst().orElseThrow(NullPointerException::new);
    }

    public InventoryAddon getSoilInventory() {
        return getController().getMultiInventory().getInvOptional().map(inv -> inv).orElse(
                new MultiItemCapHandler(new ArrayList<>())).getInventories().stream().filter(
                addon -> addon.getName().contains("soilInput")).findFirst().orElseThrow(NullPointerException::new);
    }

    public FluidTankAddon getWaterInventory() {
        return getController().getMultiTank().getTankOptional().map(tank -> tank).orElse(
                new MultiTankCapHandler(new ArrayList<>())).getFluidTanks().stream().filter(
                addon -> addon.getName().contains("waterIn")).findFirst().orElseThrow(NullPointerException::new);
    }

    @Override
    protected Map<Side, FaceConfig> setFaceControl() {
        Map<Side, FaceConfig> faceMap = new HashMap<>();
        for (Side side : Side.values()) {
            if (side == Side.FRONT || side == Side.UP) {
                faceMap.put(side, FaceConfig.NONE);
            } else {
                faceMap.put(side, FaceConfig.ENABLED);
            }
        }
        return faceMap;
    }

    private MachineController<BotariumRecipe> createController() {
        return new MachineController<>(this, () -> this.pos, BOTARIUM_DRAWABLE);
    }

    @Override
    public boolean shouldCheckForRecipe() {
        return !getCropInventory().getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == RecipeRegistry.BOTARIUM_RECIPE_TYPE && recipe instanceof BotariumRecipe;
    }

    @Override
    public BotariumRecipe castRecipe(IRecipe<?> iRecipe) {
        return (BotariumRecipe) iRecipe;
    }

    @Override
    public boolean matchRecipe(BotariumRecipe currentRecipe) {
        if (currentRecipe.getCropType().test(getCropInventory().getStackInSlot(0))) {
            if (currentRecipe.getSoilIn().test(getSoilInventory().getStackInSlot(0))) {
                if (getController().getEnergyStorage().getEnergyStored() >= currentRecipe.getRfPerTick()) {
                    FluidStack fluidIn = getWaterInventory().getFluid();
                    if (fluidIn.isFluidEqual(
                            currentRecipe.getFluidIn()) && fluidIn.getAmount() >= currentRecipe.getFluidIn().getAmount()) {
                        if (getOutputInventory().getStackInSlot(0).isEmpty()) {
                            return true;
                        } else {
                            if (getOutputInventory().getStackInSlot(
                                    0).getCount() != getOutputInventory().getStackInSlot(0).getMaxStackSize()) {
                                return currentRecipe.getRecipeOutput().isItemEqual(getOutputInventory().getStackInSlot(0));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void handleProgressFinish(BotariumRecipe currentRecipe) {
        if (world != null) {
            if (!world.isRemote()) {
                getController().getMultiTank().getTankOptional().ifPresent(
                        multiTank -> multiTank.drain(currentRecipe.getFluidIn().getAmount(),
                                IFluidHandler.FluidAction.EXECUTE));
                ItemStack currentOut = getOutputInventory().getStackInSlot(0);
                ItemStack stackIn = getCropInventory().getStackInSlot(
                        0).getCount() == 0 ? ItemStack.EMPTY : currentRecipe.getRecipeOutput();
                if (currentOut.isEmpty()) {
                    getOutputInventory().insertItem(0, stackIn, false);
                } else {
                    if (stackIn.isItemEqual(currentOut)) {
                        getOutputInventory().insertItem(0, stackIn, false);
                    }
                }
            }
        }
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
    public Map<BlockPos, MachineBlock<?>> getMachineSlaveLocations() {
        Map<BlockPos, MachineBlock<?>> map = super.getMachineSlaveLocations();
        map.put(pos.up(), BOTARIUM_TOP.get());
        return map;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}

