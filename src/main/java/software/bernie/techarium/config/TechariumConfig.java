package software.bernie.techarium.config;

import lombok.experimental.UtilityClass;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@UtilityClass
public class TechariumConfig {

    private static ForgeConfigSpec.Builder SERVER = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec SERVER_SPEC;
    //This config is synced from server to client
    public static TechariumServerConfig SERVER_CONFIG;


    public static void load() {
        generateServerConfig();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }


    private static void generateServerConfig() {
        SERVER_CONFIG = new TechariumServerConfig();
    }

    public static class TechariumServerConfig {
        public final ForgeConfigSpec.ConfigValue<Integer> numHammerForDepotRecipe;

        private TechariumServerConfig() {
            SERVER.push("General");

            numHammerForDepotRecipe = SERVER
                    .comment("The amount of times you need to hammer before the hammering recipe is complete. Hammering 5 times takes 1 second")
                    .defineInRange("numHammerings", 5, 1, Integer.MAX_VALUE);

            SERVER.pop();
            SERVER_SPEC = SERVER.build();
        }
    }
}
