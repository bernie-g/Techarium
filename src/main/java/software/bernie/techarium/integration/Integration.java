package software.bernie.techarium.integration;

import lombok.Getter;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;

import java.util.function.Supplier;

public abstract class Integration {

    public static class Wrapper<T extends Integration> {
        @Getter
        private final String modID;
        private final Lazy<T> integration;
        private LazyOptional<T> optionalIntegration = LazyOptional.empty();

        public static <T extends Integration> Wrapper<T> of(String modID, Supplier<T> integration) {
            return new Wrapper<T>(modID, Lazy.of(integration));
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
    }
}
