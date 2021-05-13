package software.bernie.techarium.integration.mysticalagriculture;

import com.blakebr0.mysticalagriculture.item.MysticalSeedsItem;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.integration.Integration;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.TagRegistry;

import java.util.function.Consumer;

public class MysticalAgricultureIntegration extends Integration {
    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof MysticalSeedsItem) {
                MysticalSeedsItem mysticalSeed = (MysticalSeedsItem) item;
                BotariumRecipe.builder()
                        .cropType(Ingredient.fromStacks(new ItemStack(mysticalSeed)))
                        .soilIn(Ingredient.fromTag(TagRegistry.DIRT))
                        .fluidIn(new FluidStack(Fluids.WATER, 1000))
                        .maxProgress(2000)
                        .rfPerTick(30)
                        .progressPerTick(1)
                        .output(new ItemStack(mysticalSeed.getCrop().getEssence(), 1))
                        .construct()
                        .addCondition(new ModLoadedCondition(ModIntegrations.MYSTICAL.getModID()))
                        .build(consumer,
                                new ResourceLocation(Techarium.ModID, "botarium/mystical/" + mysticalSeed.getRegistryName().getPath()));
            }
        }
    }
}
