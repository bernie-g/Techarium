package software.bernie.techarium.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.items.IEItems;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fluids.FluidStack;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.integration.Integration;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.recipe.recipe.BotariumRecipe;
import software.bernie.techarium.registry.TagRegistry;
import software.bernie.techarium.util.loot.ChancedItemStackList;

import java.util.function.Consumer;

public class IEIntegration extends Integration {
    public IEIntegration(String modID) {
        super(modID);
    }

    @Override
    public void generateRecipes(Consumer<IFinishedRecipe> consumer) {
        BotariumRecipe.builder()
                .cropType(Ingredient.of(new ItemStack(IEItems.Misc.hempSeeds)))
                .soilIn(Ingredient.of(TagRegistry.DIRT))
                .fluidIn(new FluidStack(Fluids.WATER, 1000))
                .maxProgress(1000)
                .rfPerTick(10)
                .progressPerTick(1)
                .output(ChancedItemStackList.of(IEItems.Ingredients.hempFiber))
                .renderSoil((BlockItem) Items.FARMLAND)
                .construct()
                .addCondition(new ModLoadedCondition(ModIntegrations.getIE().orElseThrow(NullPointerException::new).getModID()))
                .build(consumer,
                        new ResourceLocation(Techarium.ModID,
                                "botarium/immersiveengineering/" + IEItems.Misc.hempSeeds.getRegistryName().getPath()));
    }
}
