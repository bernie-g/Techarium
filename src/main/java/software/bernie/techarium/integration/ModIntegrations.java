package software.bernie.techarium.integration;

import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techarium.integration.mekanism.MekanismIntegration;

public class ModIntegrations {
    private static final Integration.Wrapper<MekanismIntegration> MEKANISM = Integration.Wrapper.of("mekanism",
            MekanismIntegration::new);

    public static LazyOptional<MekanismIntegration> getMekanism() {
        return MEKANISM.get();
    }
}