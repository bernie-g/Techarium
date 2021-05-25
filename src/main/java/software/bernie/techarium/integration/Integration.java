package software.bernie.techarium.integration;

import lombok.Getter;
import net.minecraft.data.IFinishedRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;

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
