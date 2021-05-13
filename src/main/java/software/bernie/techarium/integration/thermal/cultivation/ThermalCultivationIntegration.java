package software.bernie.techarium.integration.thermal.cultivation;

import cofh.lib.block.impl.crops.CropsBlockCoFH;
import lombok.SneakyThrows;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.integration.Integration;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.TagRegistry;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class ThermalCultivationIntegration extends Integration {
    @SneakyThrows
    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        Method getCropItemMethod = CropsBlockCoFH.class.getDeclaredMethod("getCropItem");
        Method getSeedsItemMethod = CropsBlockCoFH.class.getDeclaredMethod("getSeedsItem");
        getCropItemMethod.setAccessible(true);
        getSeedsItemMethod.setAccessible(true);

        for (Block block : ForgeRegistries.BLOCKS) {
            if (block instanceof CropsBlockCoFH) {
                CropsBlockCoFH cropsBlockCoFH = (CropsBlockCoFH) block;
                IItemProvider cropProvider = (IItemProvider) getCropItemMethod.invoke(cropsBlockCoFH);
                IItemProvider seedsProvider = (IItemProvider) getSeedsItemMethod.invoke(cropsBlockCoFH);

                BotariumRecipe.builder()
                        .cropType(Ingredient.fromStacks(new ItemStack(seedsProvider.asItem())))
                        .soilIn(Ingredient.fromTag(TagRegistry.DIRT))
                        .fluidIn(new FluidStack(Fluids.WATER, 1000))
                        .maxProgress(1000)
                        .rfPerTick(10)
                        .progressPerTick(1)
                        .output(new ItemStack(cropProvider.asItem(), 1))
                        .construct()
                        .addCondition(new ModLoadedCondition(ModIntegrations.MYSTICAL.getModID()))
                        .build(consumer,
                                new ResourceLocation(Techarium.ModID, "botarium/thermal/cultivation/" + seedsProvider.asItem().getRegistryName().getPath()));
            }
        }
    }
}
