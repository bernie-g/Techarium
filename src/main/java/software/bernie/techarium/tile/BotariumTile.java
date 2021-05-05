package software.bernie.techarium.tile;

import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
import software.bernie.techarium.registry.RecipeSerializerRegistry;
import software.bernie.techarium.tile.base.MultiblockMasterTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.block.Block.getDrops;
import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.*;
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
        getController().addController(machineController(1, BOTARIUM_BASE_TIER_1));
        getController().addController(machineController(2, BOTARIUM_BASE_TIER_2));
        getController().addController(machineController(3, BOTARIUM_BASE_TIER_3));
        getController().addController(machineController(4, BOTARIUM_BASE_TIER_4));
        getController().addController(machineController(5, BOTARIUM_BASE_TIER_5));
    }


    private MachineController<BotariumRecipe> machineController(int tier, IDrawable background) {
        MachineController<BotariumRecipe> controller = createController(tier);
        controller.setBackground(background, sizeX, sizeY);
        controller.setPowered(true);
        controller.setEnergyStorage(10000, 10000, 8, 35);

        ProgressBarAddon progressBarAddon = new ProgressBarAddon(this, 8, 26, 500, "techarium.gui.mainprogress");
        controller.addProgressBar(progressBarAddon
                .setCanProgress(
                        (value) -> {
                            BotariumRecipe recipe = getActiveController().getCurrentRecipe();
                            FluidStack fluid = controller.getMultiTank().getFluidTanks().get(0).getFluid();
                            return recipe != null && getActiveController().getEnergyStorage().getEnergyStored() > 0 && fluid.getAmount() >= recipe.getFluidIn().getAmount();
                        })
                .setOnProgressFull(() -> handleProgressFinish(getActiveController().getCurrentRecipe()))
                .setOnProgressTick(() -> {
                    if (controller.getCurrentRecipe() != null)
                        controller.getLazyEnergyStorage().ifPresent(iEnergyStorage -> iEnergyStorage.extractEnergy(
                                controller.getCurrentRecipe().getEnergyCost() / controller.getCurrentRecipe().getMaxProgress(),
                                false));
                })
        );

        controller.addTank(new FluidTankAddon(this, "waterIn", 10000 * tier, 23, 35,
                (fluidStack -> fluidStack.getFluid() instanceof WaterFluid)));

        controller.addInventory(new InventoryAddon(this, "soilInput", 49, 67, 1)
                .setInputFilter((itemStack, integer) -> itemStack.getItem().equals(Items.DIRT))
                .setOnSlotChanged((itemStack, integer) -> forceCheckRecipe()).setSlotStackSize(0, 1));

        controller.addInventory(new InventoryAddon(this, "cropInput", 49, 35, 1)
                .setInputFilter((itemStack, integer) -> world.getRecipeManager().getRecipes()
                        .stream()
                        .filter(this::checkRecipe)
                        .map(this::castRecipe).anyMatch(
                                recipe -> recipe.getCropType().getIsCropAcceptable().test(itemStack))
                ).setOnSlotChanged((itemStack, integer) -> forceCheckRecipe()).setSlotStackSize(0, 1)
        );

        controller.addInventory(new InventoryAddon(this, "upgradeSlot", 83, 81, 1 + (tier - 1))
                .setInputFilter((itemStack, integer) -> itemStack.getItem() instanceof UpgradeItem));

        controller.addInventory(
                new DrawableInventoryAddon(this, "output", 183, 49, BOTARIUM_OUTPUT_SLOT, 178, 34, 30, 46, 1)
                        .setInputFilter((itemStack, integer) -> false)
                        .setOnSlotChanged((itemStack, integer) -> forceCheckRecipe()).setSlotStackSize(0, 1));

        return controller;
    }

    public InventoryAddon getCropInventory() {
        return getActiveController().getMultiInventory().getInvOptional().map(inv -> inv).orElse(
                new MultiItemCapHandler(new ArrayList<>())).getInventories().stream().filter(
                addon -> addon.getName().contains("cropInput")).findFirst().orElseThrow(NullPointerException::new);
    }

    public InventoryAddon getOutputInventory() {
        return getActiveController().getMultiInventory().getInvOptional().map(inv -> inv).orElse(
                new MultiItemCapHandler(new ArrayList<>())).getInventories().stream().filter(
                addon -> addon.getName().contains("output")).findFirst().orElseThrow(NullPointerException::new);
    }

    public InventoryAddon getSoilInventory() {
        return getActiveController().getMultiInventory().getInvOptional().map(inv -> inv).orElse(
                new MultiItemCapHandler(new ArrayList<>())).getInventories().stream().filter(
                addon -> addon.getName().contains("soilInput")).findFirst().orElseThrow(NullPointerException::new);
    }

    public FluidTankAddon getWaterInventory() {
        return getActiveController().getMultiTank().getTankOptional().map(tank -> tank).orElse(
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

    private MachineController<BotariumRecipe> createController(int tier) {
        return new MachineController<>(this, () -> this.pos, tier);
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
        if (currentRecipe.getTier() <= getActiveController().getTier()) {
            if (currentRecipe.getCropType().getIsCropAcceptable().test(getCropInventory().getStackInSlot(0))) {
                if (currentRecipe.getSoilIn().test(getSoilInventory().getStackInSlot(0))) {
                    if (getActiveController().getEnergyStorage().getEnergyStored() >= currentRecipe.getEnergyCost()) {
                        FluidStack fluidIn = getWaterInventory().getFluid();
                        if (fluidIn.isFluidEqual(
                                currentRecipe.getFluidIn()) && fluidIn.getAmount() >= currentRecipe.getFluidIn().getAmount()) {
                            if (getOutputInventory().getStackInSlot(0).isEmpty()) {
                                return true;
                            } else {
                                if (getOutputInventory().getStackInSlot(
                                        0).getCount() != getOutputInventory().getStackInSlot(0).getMaxStackSize()) {
                                    return getOutputInventory().getStackInSlot(0).isItemEqual(
                                            getCropFromGecko(world, getCropInventory().getStackInSlot(0)));
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
                getActiveController().getMultiTank().getTankOptional().ifPresent(
                        multiTank -> multiTank.drain(currentRecipe.getFluidIn().getAmount(),
                                IFluidHandler.FluidAction.EXECUTE));
                ItemStack currentOut = getOutputInventory().getStackInSlot(0);
                ItemStack stackIn = getCropInventory().getStackInSlot(
                        0).getCount() == 0 ? ItemStack.EMPTY : getCropFromGecko(world,
                        getCropInventory().getStackInSlot(0));
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
                List<ItemStack> itemStacks = getDrops(block.getDefaultState().with(cropProperty, crop.getMaxAge()),
                        (ServerWorld) world, BlockPos.ZERO, null);
                return itemStacks.get(0);
            } else if (block instanceof StemBlock) {
                StemBlock crop = (StemBlock) block;
                return Item.getItemFromBlock(crop.getCrop()).getDefaultInstance();
            }
        }
        return ItemStack.EMPTY;
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

