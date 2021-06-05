package software.bernie.techarium.integration.pams;

import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import pam.pamhc2crops.items.ItemPamSeed;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.integration.Integration;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.recipes.recipe.BotariumRecipe;
import software.bernie.techarium.registry.TagRegistry;

import java.util.function.Consumer;

public class PamsHarvestCraftIntegration extends Integration {
    public PamsHarvestCraftIntegration(String modID) {
        super(modID);
    }

    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof ItemPamSeed) {
                ItemPamSeed seed = (ItemPamSeed) item;
                Block block = seed.getBlock();
                Item drop = getPamsDropFromCrop(block);
                BotariumRecipe.builder()
                        .cropType(Ingredient.of(new ItemStack(seed)))
                        .soilIn(Ingredient.of(TagRegistry.DIRT))
                        .fluidIn(new FluidStack(Fluids.WATER, 1000))
                        .maxProgress(1000)
                        .rfPerTick(10)
                        .progressPerTick(1)
                        .output(new ItemStack(drop, 1))
                        .construct()
                        .addCondition(new ModLoadedCondition(ModIntegrations.getPams().orElseThrow(NullPointerException::new).getModID()))
                        .build(consumer,
                                new ResourceLocation(Techarium.ModID,
                                        "botarium/pams/" + seed.getRegistryName().getPath()));
            }
        }
    }

    private Item getPamsDropFromCrop(Block block) {
        for (Item item : ForgeRegistries.ITEMS) {
            if(!(item instanceof BlockItem)) continue;
            BlockItem blockItem = (BlockItem) item;
            if(!(blockItem instanceof ItemPamSeed) && blockItem.getBlock() == block)
            {
                return blockItem;
            }
        }

        //should never happen
        return null;
    }
}
