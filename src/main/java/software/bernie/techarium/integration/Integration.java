package software.bernie.techarium.integration;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.recipe.recipe.ArboretumRecipe;
import software.bernie.techarium.recipe.recipe.BotariumRecipe;
import software.bernie.techarium.registry.TagRegistry;
import software.bernie.techarium.util.ChancedItemStack;
import software.bernie.techarium.util.ChancedItemStackList;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public abstract class Integration {

    @Getter
    private final String modID;

    public Integration(String modID) {
        MinecraftForge.EVENT_BUS.register(this);
        this.modID = modID;
    }

    /**
     * Used for datagenning.
     *
     * @param finishedRecipeConsumer
     */
    public void generateRecipes(Consumer<IFinishedRecipe> finishedRecipeConsumer) {

    }

    public void buildBotariumFlowerRecipe(FlowerBlock flowerBlock, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(flowerBlock.asItem(), ChancedItemStackList.of(new ItemStack(flowerBlock)), Ingredient
                .of(Blocks.GRASS_BLOCK), 1000, 1000, consumer);
    }
    public void buildBotariumRecipe(Item seed, ChancedItemStackList drop, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, Ingredient.of(TagRegistry.DIRT), null, amountWater, time, consumer);
    }
    public void buildBotariumFarmlandRecipe(Item seed, ChancedItemStackList drop, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, Ingredient.of(TagRegistry.DIRT), (BlockItem) Items.FARMLAND, amountWater, time, consumer);
    }
    public void buildBotariumRecipe(Item seed, ChancedItemStackList drop, Ingredient soil, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, soil, null, new FluidStack(Fluids.WATER, amountWater), time, consumer);
    }
    public void buildBotariumRecipe(Item seed, ChancedItemStackList drop, Ingredient soil, BlockItem renderSoil, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, soil, renderSoil, new FluidStack(Fluids.WATER, amountWater), time, consumer);
    }

    public void buildBotariumRecipe(Item seed, ChancedItemStackList drop, Ingredient soil, BlockItem renderSoil, FluidStack fluid, int time, Consumer<IFinishedRecipe> consumer) {
        BotariumRecipe.builder()
                .cropType(Ingredient.of(seed))
                .soilIn(soil)
                .renderSoil(renderSoil)
                .fluidIn(fluid)
                .maxProgress(time)
                .rfPerTick(10)
                .progressPerTick(1)
                .output(drop)
                .construct()
                .addCondition(new ModLoadedCondition(modID))
                .build(consumer,
                        new ResourceLocation(Techarium.ModID,
                                "botarium/" + seed.getRegistryName().getNamespace() + "/" + seed.getRegistryName()
                                        .getPath()));
    }

    public void buildArboretumRecipe(Item sapling, ChancedItemStackList drop, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildArboretumRecipe(sapling, drop, Ingredient.of(TagRegistry.DIRT), amountWater, time, consumer);
    }

    public void buildArboretumRecipe(Item sapling, ChancedItemStackList drop, Ingredient soil, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildArboretumRecipe(sapling, drop, soil, new FluidStack(Fluids.WATER, amountWater), time, consumer);
    }

    private void buildArboretumRecipe(Item sapling, ChancedItemStackList drop, Ingredient soil, FluidStack fluid, int time, Consumer<IFinishedRecipe> consumer) {
        ArboretumRecipe.builder()
                .cropType(Ingredient.of(sapling))
                .soilIn(soil)
                .fluidIn(fluid)
                .maxProgress(time)
                .rfPerTick(10)
                .progressPerTick(1)
                .output(drop)
                .construct()
                .addCondition(new ModLoadedCondition(modID))
                .build(consumer,
                        new ResourceLocation(Techarium.ModID,
                                "arboretum/" + modID + "/" + sapling.getRegistryName().getPath()));
    }

    public static class Wrapper<T extends Integration> {
        @Getter
        private final String modID;
        private final Lazy<T> integration;
        private LazyOptional<T> optionalIntegration = LazyOptional.empty();

        public static <T extends Integration> Wrapper<T> of(String modID, Class<T> integration) {
            return new Wrapper<>(modID, Lazy.of(() -> {
                try {
                    return integration.getConstructor(String.class).newInstance(modID);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }));
        }

        public Wrapper(String modID, Lazy<T> integration) {
            this.modID = modID;
            this.integration = integration;
        }

        public boolean isPresent() {
            return ModList.get().isLoaded(this.getModID());
        }

        public LazyOptional<T> get() {
            if (isPresent()) {
                optionalIntegration = LazyOptional.of(integration::get);
            }
            return optionalIntegration;
        }

        public Wrapper<T> registerSelf() {
            ModIntegrations.getIntegrations().add(this);
            return this;
        }
    }

    @FunctionalInterface
    public interface IntegrationProvider<T extends Integration> {
        T create(String modID);
    }
}
