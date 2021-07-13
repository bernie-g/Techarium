package software.bernie.techarium.integration;

import lombok.Getter;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.techarium.integration.biomesoplenty.BiomesOPlentyIntegration;
import software.bernie.techarium.integration.byg.BYGIntegration;
import software.bernie.techarium.integration.farmersdelight.FarmersDelightIntegration;
import software.bernie.techarium.integration.immersiveengineering.IEIntegration;
import software.bernie.techarium.integration.mekanism.MekanismIntegration;
import software.bernie.techarium.integration.mysticalagriculture.MysticalAgricultureIntegration;
import software.bernie.techarium.integration.pams.PamsHarvestCraftIntegration;
import software.bernie.techarium.integration.theoneprobe.TheOneProbeIntegration;
import software.bernie.techarium.integration.thermal.cultivation.ThermalCultivationIntegration;
import software.bernie.techarium.integration.xlfoodmod.XLFoodModIntegration;

import java.util.ArrayList;
import java.util.List;

public class ModIntegrations {
    @Getter
    private static final List<Integration.Wrapper<?>> integrations = new ArrayList<>();

    private static final Integration.Wrapper<MekanismIntegration> MEKANISM = Integration.Wrapper.of("mekanism",
            MekanismIntegration.class).registerSelf();

    private static final Integration.Wrapper<MysticalAgricultureIntegration> MYSTICAL = Integration.Wrapper.of(
            "mysticalagriculture",
            MysticalAgricultureIntegration.class).registerSelf();

    private static final Integration.Wrapper<PamsHarvestCraftIntegration> PAMS = Integration.Wrapper.of("pamhc2crops",
            PamsHarvestCraftIntegration.class).registerSelf();

    private static final Integration.Wrapper<ThermalCultivationIntegration> THERMAL_CULTIVATION = Integration.Wrapper.of("thermal_cultivation",
            ThermalCultivationIntegration.class).registerSelf();

    private static final Integration.Wrapper<TheOneProbeIntegration> THE_ONE_PROBE = Integration.Wrapper.of("theoneprobe",
            TheOneProbeIntegration.class).registerSelf();

    private static final Integration.Wrapper<FarmersDelightIntegration> FARMERS_DELIGHT = Integration.Wrapper.of("farmersdelight",
            FarmersDelightIntegration.class).registerSelf();

    private static final Integration.Wrapper<XLFoodModIntegration> XL_FOOD_MOD = Integration.Wrapper.of("xlfoodmod",
            XLFoodModIntegration.class).registerSelf();

    private static final Integration.Wrapper<IEIntegration> IMMERSIVE_ENGINEERING = Integration.Wrapper.of("immersive_engineering",
            IEIntegration.class).registerSelf();

    private static final Integration.Wrapper<BiomesOPlentyIntegration> BIOMES_O_PLENTY = Integration.Wrapper.of("biomesoplenty",
            BiomesOPlentyIntegration.class).registerSelf();

    private static final Integration.Wrapper<BYGIntegration> BYG = Integration.Wrapper.of("byg",
            BYGIntegration.class).registerSelf();



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

    public static LazyOptional<FarmersDelightIntegration> getFarmersDelight() {
        return FARMERS_DELIGHT.get();
    }

    public static LazyOptional<TheOneProbeIntegration> getTheOneProbe() {
        return THE_ONE_PROBE.get();
    }

    public static LazyOptional<IEIntegration> getIE() {
        return IMMERSIVE_ENGINEERING.get();
    }

    public static LazyOptional<XLFoodModIntegration> getXLFoodMod() {
        return XL_FOOD_MOD.get();
    }

    public static LazyOptional<BiomesOPlentyIntegration> getBiomesOPlenty() {
        return BIOMES_O_PLENTY.get();
    }

    public static LazyOptional<BYGIntegration> getBYG() {
        return BYG.get();
    }
}