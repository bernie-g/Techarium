package software.bernie.techarium.integration;

import net.minecraftforge.fml.ModList;

public abstract class Integration {
    private final String modID;

    protected Integration(String modID) {
        this.modID = modID;
    }

    public boolean isPresent() {
        return ModList.get().isLoaded(this.getModID());
    }

    public String getModID() {
        return this.modID;
    }

    public static class Wrapper<T extends Integration> {
        private final T integration;

        public static <T extends Integration> Wrapper<T> of(T integration) {
            return new Wrapper(integration);
        }

        public Wrapper(T integration) {
            this.integration = integration;
        }

        public boolean isPresent() {
            return integration.isPresent();
        }

        public T get() {
            if (isPresent()) {
                return this.integration;
            } else {
                throw new ModIntegrationException(integration.getModID());
            }
        }
    }
}
