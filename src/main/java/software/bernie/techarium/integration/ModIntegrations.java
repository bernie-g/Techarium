package software.bernie.techarium.integration;

import lombok.Getter;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techarium.integration.mekanism.MekanismIntegration;
import software.bernie.techarium.integration.mysticalagriculture.MysticalAgricultureIntegration;
import software.bernie.techarium.integration.pams.PamsHarvestCraftIntegration;
import software.bernie.techarium.integration.thermal.cultivation.ThermalCultivationIntegration;

import java.util.ArrayList;
import java.util.List;

public class ModIntegrations {
    @Getter
    private static final List<Integration.Wrapper<?>> integrations = new ArrayList<>();

    public static final Integration.Wrapper<MekanismIntegration> MEKANISM = Integration.Wrapper.of("mekanism",
            MekanismIntegration::new).registerSelf();

    public static final Integration.Wrapper<MysticalAgricultureIntegration> MYSTICAL = Integration.Wrapper.of(
            "mysticalagriculture",
            MysticalAgricultureIntegration::new).registerSelf();

    public static final Integration.Wrapper<PamsHarvestCraftIntegration> PAMS = Integration.Wrapper.of("pamhc2crops",
            PamsHarvestCraftIntegration::new).registerSelf();

    public static final Integration.Wrapper<ThermalCultivationIntegration> THERMAL_CULTIVATION = Integration.Wrapper.of("thermal",
            ThermalCultivationIntegration::new).registerSelf();

    public static LazyOptional<MekanismIntegration> getMekanism() {
        return MEKANISM.get();
    }

    public static LazyOptional<MysticalAgricultureIntegration> getMystical() {
        return MYSTICAL.get();
    }

    public static LazyOptional<PamsHarvestCraftIntegration> getPams() {
        return PAMS.get();
    }

    public static LazyOptional<ThermalCultivationIntegration> getThermalCultivation() {
        return THERMAL_CULTIVATION.get();
    }
}