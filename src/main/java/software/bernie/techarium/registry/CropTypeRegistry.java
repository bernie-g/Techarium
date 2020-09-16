package software.bernie.techarium.registry;

import net.minecraft.block.CropsBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.item.BlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import software.bernie.techarium.api.CropType;

import java.util.Objects;

import static software.bernie.techarium.Techarium.ModID;

public class CropTypeRegistry {

    private static final DeferredRegister<CropType> CROP_TYPE = DeferredRegister.create(TechariumCustomRegistries.CROP_TYPE,ModID);

    public static final RegistryObject<CropType> VANILLA_CROP = CROP_TYPE.register("vanilla_crop",
            ()-> new CropType((stack)-> stack.getItem() instanceof BlockItem &&
            ((BlockItem) stack.getItem()).getBlock() instanceof CropsBlock &&
                    Objects.requireNonNull(stack.getItem().getRegistryName()).toString().contains("minecraft")));

    public static final RegistryObject<CropType> VANILLA_STEM = CROP_TYPE.register("vanilla_stem",
            ()-> new CropType((stack)-> stack.getItem() instanceof BlockItem &&
                    ((BlockItem) stack.getItem()).getBlock() instanceof StemBlock &&
                    Objects.requireNonNull(stack.getItem().getRegistryName()).toString().contains("minecraft")));


    public static void register(IEventBus bus){
        CROP_TYPE.register(bus);
    }

}
