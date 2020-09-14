package software.bernie.techarium.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import software.bernie.techarium.api.CropType;

import static software.bernie.techarium.Techarium.ModID;

public class TechariumCustomRegistries {

    static {
        init();
    }

    public static IForgeRegistry<CropType> CROP_TYPE = RegistryManager.ACTIVE.getRegistry(CropType.class);

    private static void init() {
        new RegistryBuilder<CropType>()
                .setName(new ResourceLocation(ModID, "croptype"))
                .setType(CropType.class)
                .setDefaultKey(new ResourceLocation(ModID, "empty"))
                .create();
    }

}
