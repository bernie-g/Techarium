package software.bernie.techarium.tile;

import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.state.IntegerProperty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.block.SpecialAnimationController;
import software.bernie.geckolib.entity.IAnimatable;
import software.bernie.geckolib.event.predicate.SpecialAnimationPredicate;
import software.bernie.geckolib.manager.AnimationManager;
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
import software.bernie.techarium.registry.RecipeSerializerRegistry;
import software.bernie.techarium.tile.base.MachineMasterTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.block.Block.getDrops;
import static net.minecraft.block.CropsBlock.AGE;
import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.*;
import static software.bernie.techarium.registry.BlockTileRegistry.BOTARIUM;

public class BotariumTile extends MachineMasterTile<BotariumRecipe> implements IAnimatable {

    private final int sizeX = 172;
    private final int sizeY = 184;

    private AnimationManager manager = new AnimationManager();
    private SpecialAnimationController controller = new SpecialAnimationController(this, "controller", 0, this::animationPredicate);

    public boolean isOpening = false;

    private <E extends IAnimatable> boolean animationPredicate(SpecialAnimationPredicate<E> event) {
        if (isOpening) {
            this.controller.setAnimation(new AnimationBuilder().addAnimation("Botarium.anim.deploy", false).addAnimation("Botarium.anim.idle", true));
        } else {
            this.controller.setAnimation(new AnimationBuilder().addAnimation("Botarium.anim.idle", true));
        }
        return true;
    }

    public BotariumTile() {
        super(BOTARIUM.getTileEntityType());
        getController().addController(machineController(1, BOTARIUM_BASE_TIER_1));
        getController().addController(machineController(2, BOTARIUM_BASE_TIER_2));
        getController().addController(machineController(3, BOTARIUM_BASE_TIER_3));
        getController().addController(machineController(4, BOTARIUM_BASE_TIER_4));
        getController().addController(machineController(5, BOTARIUM_BASE_TIER_5));
        this.manager.addAnimationController(controller);
    }


    private MachineController<BotariumRecipe> machineController(int tier, IDrawable background) {
        MachineController<BotariumRecipe> controller = createController(tier);
        controller.setBackground(background, sizeX, sizeY);
        controller.setPowered(true);
        controller.setEnergyStorage(10000, 10000, 8, 35);

        controller.addProgressBar(new ProgressBarAddon(this, 8, 26, 500, "techarium.gui.mainprogress")
                .setCanProgress((value) -> getActiveController().getCurrentRecipe() != null)
                .setOnProgressFull(() -> handleProgressFinish(getActiveController().getCurrentRecipe()))
        );

        controller.addTank(new FluidTankAddon(this, "waterIn", 10000 * tier, 23, 35));

        controller.addInventory(new InventoryAddon(this, "soilInput", 49, 67, 1)
                .setInputFilter((itemStack, integer) -> itemStack.getItem().equals(Items.DIRT))
                .setOnSlotChanged((itemStack, integer) -> forceCheckRecipe()).setSlotStackSize(0, 1));

        controller.addInventory(new InventoryAddon(this, "cropInput", 49, 35, 1)
                .setInputFilter((itemStack, integer) -> world.getRecipeManager().getRecipes()
                        .stream()
                        .filter(this::checkRecipe)
                        .map(this::castRecipe).anyMatch(recipe -> recipe.getCropType().getIsCropAcceptable().test(itemStack))
                ).setOnSlotChanged((itemStack, integer) -> forceCheckRecipe())
        );

        controller.addInventory(new InventoryAddon(this, "upgradeSlot", 83, 81, 1 + (tier - 1))
                .setInputFilter((itemStack, integer) -> itemStack.getItem() instanceof UpgradeItem));

        controller.addInventory(new DrawableInventoryAddon(this, "output", 183, 49, BOTARIUM_OUTPUT_SLOT, 178, 34, 30, 46, 1)
                .setInputFilter((itemStack, integer) -> false));

        return controller;
    }

    public InventoryAddon getCropInventory() {
        return getActiveController().getMultiInventory().getInvOptional().map(inv -> inv).orElse(new MultiItemCapHandler(new ArrayList<>())).getInventories().stream().filter(addon -> addon.getName().contains("cropInput")).findFirst().orElseThrow(NullPointerException::new);
    }

    public InventoryAddon getOutputInventory() {
        return getActiveController().getMultiInventory().getInvOptional().map(inv -> inv).orElse(new MultiItemCapHandler(new ArrayList<>())).getInventories().stream().filter(addon -> addon.getName().contains("output")).findFirst().orElseThrow(NullPointerException::new);
    }

    public InventoryAddon getSoilInventory() {
        return getActiveController().getMultiInventory().getInvOptional().map(inv -> inv).orElse(new MultiItemCapHandler(new ArrayList<>())).getInventories().stream().filter(addon -> addon.getName().contains("soilInput")).findFirst().orElseThrow(NullPointerException::new);
    }

    public FluidTankAddon getWaterInventory() {
        return getActiveController().getMultiTank().getTankOptional().map(tank -> tank).orElse(new MultiTankCapHandler(new ArrayList<>())).getFluidTanks().stream().filter(addon -> addon.getName().contains("waterIn")).findFirst().orElseThrow(NullPointerException::new);
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

    private MachineController<BotariumRecipe> createController(int tier) {
        return new MachineController<>(this, () -> this.pos, tier);
    }

    @Override
    public AnimationManager getAnimationManager() {
        return this.manager;
    }

    @Override
    public boolean shouldCheckForRecipe() {
        return !getCropInventory().getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == RecipeSerializerRegistry.BOTARIUM_RECIPE_TYPE && recipe instanceof BotariumRecipe;
    }

    @Override
    public BotariumRecipe castRecipe(IRecipe<?> iRecipe) {
        return (BotariumRecipe) iRecipe;
    }

    @Override
    public boolean matchRecipe(BotariumRecipe currentRecipe) {
        if (currentRecipe.getTier() == getActiveController().getTier()) {
            if (currentRecipe.getCropType().getIsCropAcceptable().test(getCropInventory().getStackInSlot(0))) {
                if (currentRecipe.getSoilIn().test(getSoilInventory().getStackInSlot(0))) {
                    if (getActiveController().getEnergyStorage().getEnergyStored() >= currentRecipe.getEnergyCost()) {
                        FluidStack fluidIn = getWaterInventory().getFluid();
                        if (fluidIn.isFluidEqual(currentRecipe.getFluidIn()) && fluidIn.getAmount() >= currentRecipe.getFluidIn().getAmount()) {
                            if (getOutputInventory().getStackInSlot(0).isEmpty()) {
                                return true;
                            } else {
                                if (getOutputInventory().getStackInSlot(0).getCount() != getOutputInventory().getStackInSlot(0).getMaxStackSize()) {
                                    return getOutputInventory().getStackInSlot(0).isItemEqual(getCropFromGecko(getCropInventory().getStackInSlot(0)));
                                }
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
                getActiveController().getLazyEnergyStorage().ifPresent(energy -> energy.extractEnergy(currentRecipe.getEnergyCost(), false));
                getActiveController().getMultiTank().getTankOptional().ifPresent(multiTank -> multiTank.drain(currentRecipe.getFluidIn().getAmount(), IFluidHandler.FluidAction.EXECUTE));
                ItemStack currentOut = getOutputInventory().getStackInSlot(0);
                ItemStack stackIn = getCropFromGecko(getCropInventory().getStackInSlot(0));
                if (currentOut.isEmpty()) {
                    getOutputInventory().insertItem(0, stackIn, false);
                } else {
                    if (stackIn.isItemEqual(currentOut)) {
                        getOutputInventory().insertItem(0, stackIn, false);
                    }
                }
                getCropInventory().extractItem(0, 1, false);
            }
        }
    }

    @Override
    public void forceCheckRecipe() {
        if (getActiveController().getCurrentRecipe() != null) {
            if (!matchRecipe(castRecipe(getActiveController().getCurrentRecipe()))) {
                getActiveController().resetCurrentRecipe();
            }
        } else {
            getActiveController().setShouldCheckRecipe();
        }
        updateMachineTile();
    }

    public static ItemStack getCropFromGecko(World world, ItemStack stack) {
        Block block = ((BlockItem) stack.getItem()).getBlock();
        if (!world.isRemote()) {
            if (block instanceof CropsBlock) {
                CropsBlock crop = (CropsBlock) block;
                IntegerProperty cropProperty = crop.getAgeProperty();
                List<ItemStack> itemStacks = getDrops(block.getDefaultState().with(cropProperty, crop.getMaxAge()), (ServerWorld) world, pos, this);
                return itemStacks.get(0);
            } else if(block instanceof StemBlock){
                StemBlock crop = (StemBlock) block;
            }
        }
        return ItemStack.EMPTY;
    }

}

