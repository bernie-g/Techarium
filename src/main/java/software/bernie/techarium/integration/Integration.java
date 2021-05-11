package software.bernie.techarium.integration;

import lombok.Getter;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.ModList;

import java.util.function.Supplier;

public abstract class Integration {

    public static class Wrapper<T extends Integration> {
        @Getter
        private static String modID;
        private final Lazy<T> integration;

        public static <T extends Integration> Wrapper<T> of(String modID, Supplier<T> integration) {
            Wrapper.modID = modID;
            return new Wrapper(Lazy.of(integration));
        }

        public Wrapper(Lazy<T> integration) {
            this.integration = integration;
        }

        public boolean isPresent() {
            return ModList.get().isLoaded(this.getModID());
        }

        public T get() {
            if (isPresent()) {
                return this.integration.get();
            } else {
                throw new ModIntegrationException(this.getModID());
            }
        }
    }
}
