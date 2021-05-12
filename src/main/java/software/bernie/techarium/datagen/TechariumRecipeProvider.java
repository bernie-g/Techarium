package software.bernie.techarium.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import software.bernie.techarium.integration.ModIntegrations;

import java.util.function.Consumer;

public class TechariumRecipeProvider extends ForgeRecipeProvider {
    public TechariumRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        ModIntegrations.getIntegrations().forEach(wrapper -> wrapper.get().ifPresent(o -> o.generateRecipes(consumer)));
    }
}
