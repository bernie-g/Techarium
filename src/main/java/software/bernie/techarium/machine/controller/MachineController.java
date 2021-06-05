package software.bernie.techarium.machine.controller;

import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.machine.addon.fluid.MultiFluidTankAddon;
import software.bernie.techarium.machine.addon.inventory.InventoryAddon;
import software.bernie.techarium.machine.addon.inventory.MultiInventoryAddon;
import software.bernie.techarium.machine.addon.progressbar.MultiProgressBarAddon;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;
import software.bernie.techarium.machine.interfaces.IContainerComponentProvider;
import software.bernie.techarium.machine.interfaces.IFactory;
import software.bernie.techarium.machine.interfaces.recipe.IMachineRecipe;
import software.bernie.techarium.recipes.AbstractMachineRecipe;
import software.bernie.techarium.tile.base.MachineMasterTile;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class MachineController<T extends IMachineRecipe> implements IContainerComponentProvider, INBTSerializable<CompoundNBT> {

    protected final Supplier<BlockPos> posSupplier;
    protected final MachineMasterTile<T> tile;

    private T currentRecipe;
    private boolean shouldCheckRecipe;
    private int recipeCheckTimer;

    private Pair<Integer, Integer> backgroundSizeXY;

    private IDrawable background;

    private Map<Integer, Pair<Integer, Integer>> playerInvSlotsXY = new HashMap<>();
    private Map<Integer, Pair<Integer, Integer>> playerHotbarSlotsXY = new HashMap<>();
    private boolean isPowered;
    private EnergyStorageAddon energyStorage;
    private final LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.of(this::getEnergyStorage);

    private MultiInventoryAddon multiInventory = new MultiInventoryAddon();
    private MultiFluidTankAddon multiTank = new MultiFluidTankAddon();
    private MultiProgressBarAddon multiProgressBar = new MultiProgressBarAddon();

    private ResourceLocation currentRecipeLocation = null;

    public MachineController(MachineMasterTile<T> tile, Supplier<BlockPos> posSupplier, IDrawable background) {
        this.posSupplier = posSupplier;
        this.tile = tile;
        this.background = background;
        this.backgroundSizeXY = Pair.of(204, 183);
        this.isPowered = false;
    }

    public void addInventory(InventoryAddon invAddon) {
        this.multiInventory.add(invAddon);
    }

    public void addTank(FluidTankAddon fluidAddon) {
        this.multiTank.add(fluidAddon);
    }

    public void addProgressBar(ProgressBarAddon progressBarAddon) {
        this.multiProgressBar.add(progressBarAddon);
    }

    public T getCurrentRecipe() {
        return currentRecipe;
    }

    @Nonnull
    public EnergyStorageAddon getEnergyStorage() {
        return energyStorage;
    }

    public IDrawable getBackground() {
        return background;
    }

    public boolean isPowered() {
        return isPowered;
    }

    public void setPowered(boolean powered) {
        isPowered = powered;
    }

    public void setEnergyStorage(int capacity, int maxIO, int xPos, int yPos) {
        int newX = xPos;
        if (backgroundSizeXY.getKey() % 2 != 0) {
            newX++;
        }
        energyStorage = new EnergyStorageAddon((MachineController<? extends AbstractMachineRecipe>) this, capacity, maxIO, newX, yPos, backgroundSizeXY);
    }

    public Pair<Integer, Integer> getBackgroundSizeXY() {
        return backgroundSizeXY;
    }

    public void setBackground(IDrawable background, int sizeX, int sizeY) {
        this.backgroundSizeXY = Pair.of(sizeX, sizeY);
        this.background = background;
    }

    public Map<Integer, Pair<Integer, Integer>> getPlayerInvSlotsXY() {
        if (playerInvSlotsXY.isEmpty()) {
            return getNormalSlotLocations();
        }
        return playerInvSlotsXY;
    }

    public Map<Integer, Pair<Integer, Integer>> getPlayerHotBarSlotsXY() {
        if (playerHotbarSlotsXY.isEmpty()) {
            return getNormalHotBarLocations();
        }
        return playerHotbarSlotsXY;
    }

    public void setPlayerInvSlotsXY(Map<Integer, Pair<Integer, Integer>> playerInvSlotsXY) {
        this.playerInvSlotsXY = playerInvSlotsXY;
    }

    private Map<Integer, Pair<Integer, Integer>> getNormalSlotLocations() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                playerInvSlotsXY.put(j + i * 9 + 9, Pair.of(7 + j * 18, 103 + i * 18));
            }
        }
        return playerInvSlotsXY;
    }


    private Map<Integer, Pair<Integer, Integer>> getNormalHotBarLocations() {
        for (int i1 = 0; i1 < 9; ++i1) {
            playerHotbarSlotsXY.put(i1, Pair.of(7 + i1 * 18, 160));
        }
        return playerHotbarSlotsXY;
    }

    public MultiInventoryAddon getMultiInventory() {
        return multiInventory;
    }

    public MultiFluidTankAddon getMultiTank() {
        return multiTank;
    }

    public LazyOptional<IEnergyStorage> getLazyEnergyStorage() {
        return lazyEnergyStorage;
    }

    public MultiProgressBarAddon getMultiProgressBar() {
        return multiProgressBar;
    }

    public void resetCurrentRecipe() {
        this.currentRecipe = null;
    }

    public void setShouldCheckRecipe() {
        shouldCheckRecipe = true;
    }

    @Override
    public List<IFactory<? extends Slot>> getContainerComponents() {
        List<IFactory<? extends Slot>> components = new ArrayList<>();
        if (getMultiInventory() != null) {
            components.addAll(getMultiInventory().getContainerComponents());
        }
        if (getMultiTank() != null) {
            components.addAll(getMultiTank().getContainerComponents());
        }
        if (getMultiProgressBar() != null) {
            components.addAll(getMultiProgressBar().getContainerComponents());
        }
        return components;
    }

    public void tick() {
        if (multiProgressBar != null) {
            this.multiProgressBar.attemptTickAllBars();
        }

        if(currentRecipeLocation != null)
        {
            this.currentRecipe = (T) this.tile.getLevel().getRecipeManager().byKey(currentRecipeLocation).orElse(null);
            currentRecipeLocation = null;
        }

        if (currentRecipe == null) {
            handleRecipeNull(shouldCheckRecipe);
        }
        shouldCheckRecipe = false;
    }

    private void handleRecipeNull(boolean shouldCheckRecipe) {
        if (recipeCheckTimer-- <= 0 || shouldCheckRecipe) {
            recipeCheckTimer = 50;
            if (tile.shouldCheckForRecipe()) {
                currentRecipe = tile.getLevel().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(tile::checkRecipe)
                        .map(tile::castRecipe)
                        .filter(tile::matchRecipe)
                        .findFirst()
                        .orElse(null);
                this.getMultiProgressBar().getProgressBarAddons().forEach(bar -> bar.setProgress(0));
                if (currentRecipe != null) {
                    AtomicInteger x = new AtomicInteger();
                    this.getMultiProgressBar().getProgressBarAddons().forEach(bar -> {
                        bar.setMaxProgress(currentRecipe.getMaxProgressTimes().get(x.get()));
                        bar.setProgressToAdd(currentRecipe.getProgressPerTick());
                        x.incrementAndGet();
                    });
                }
            }
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        getLazyEnergyStorage().ifPresent(storage -> nbt.put("energy", ((EnergyStorageAddon) storage).serializeNBT()));
        getMultiTank().getTankOptional().ifPresent(multiTank -> multiTank.getFluidTanks().forEach(
                tank -> nbt.put(tank.getName(), tank.writeToNBT(new CompoundNBT()))));
        getMultiInventory().getInvOptional().ifPresent(
                multiInv -> multiInv.getInventories().forEach(inv -> nbt.put(inv.getName(), inv.serializeNBT())));
        getMultiProgressBar().getProgressBarAddons().forEach(bar -> nbt.put(bar.getName(), bar.serializeNBT()));
        if (currentRecipe != null)
            nbt.putString("currentRecipe", currentRecipe.getId().toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        getLazyEnergyStorage().ifPresent(
                storage -> ((EnergyStorageAddon) storage).deserializeNBT(nbt.getCompound("energy")));
        getMultiTank().getTankOptional().ifPresent(multiTank -> multiTank.getFluidTanks().forEach(
                tank -> tank.readFromNBT(nbt.getCompound(tank.getName()))));
        getMultiInventory().getInvOptional().ifPresent(multiInv -> multiInv.getInventories().forEach(
                inv -> inv.deserializeNBT(nbt.getCompound(inv.getName()))));
        getMultiProgressBar().getProgressBarAddons().forEach(bar -> bar.deserializeNBT(nbt.getCompound(bar.getName())));
        if (nbt.contains("currentRecipe"))
            this.currentRecipeLocation = new ResourceLocation(nbt.getString("currentRecipe"));
    }
}
