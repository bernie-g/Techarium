package software.bernie.techarium.integration;

import lombok.Getter;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.TagRegistry;

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
        buildBotariumRecipe(flowerBlock.asItem(), flowerBlock, Ingredient.of(Blocks.GRASS_BLOCK), 1000, 1000, consumer);
    }

    public void buildBotariumRecipe(Item seed, IItemProvider drop, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, Ingredient.of(TagRegistry.DIRT), amountWater, time, consumer);
    }

    public void buildBotariumRecipe(Item seed, IItemProvider drop, Ingredient soil, int amountWater, int time, Consumer<IFinishedRecipe> consumer) {
        buildBotariumRecipe(seed, drop, soil, new FluidStack(Fluids.WATER, amountWater), time, consumer);
    }

    private void buildBotariumRecipe(Item seed, IItemProvider drop, Ingredient soil, FluidStack fluid, int time, Consumer<IFinishedRecipe> consumer) {
        BotariumRecipe.builder()
                .cropType(Ingredient.of(seed))
                .soilIn(soil)
                .fluidIn(fluid)
                .maxProgress(time)
                .rfPerTick(10)
                .progressPerTick(1)
                .output(new ItemStack(drop, 1))
                .construct()
                .addCondition(new ModLoadedCondition(modID))
                .build(consumer,
                        new ResourceLocation(Techarium.ModID,
                                "botarium/" + modID + "/" + seed.getRegistryName().getPath()));
    }

    public static class Wrapper<T extends Integration> {
        @Getter
        private final String modID;
        private final Lazy<T> integration;
        private LazyOptional<T> optionalIntegration = LazyOptional.empty();

        public static <T extends Integration> Wrapper<T> of(String modID, IntegrationProvider<T> integration) {
            return new Wrapper<T>(modID, Lazy.of(() -> integration.create(modID)));
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
