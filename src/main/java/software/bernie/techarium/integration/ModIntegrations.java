package software.bernie.techarium.integration;

import software.bernie.techarium.integration.mekanism.MekanismIntegration;

public class ModIntegrations {
    public static final Integration.Wrapper<MekanismIntegration> MEKANISM = Integration.Wrapper.of("mekanism", () -> new MekanismIntegration());
}
