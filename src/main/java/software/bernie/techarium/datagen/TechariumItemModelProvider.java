package software.bernie.techarium.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import software.bernie.techarium.datagen.base.TechariumItemModelProviderBase;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.ItemRegistry;

public class TechariumItemModelProvider extends TechariumItemModelProviderBase {

    public TechariumItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerPipes();
        registerIngots();

        simpleTexture(ItemRegistry.DEBUGSTICK.get(), "item/pipe/pipe_stick");
        simpleTexture(BlockRegistry.EXCHANGE_STATION.getItem(), "item/exchange_station");
        emptyModel("multi_pipe");
    }

    public void registerPipes() {
        simpleTexture(ItemRegistry.ENERGY_PIPE.get(), "item/pipe/energy_pipe");
        simpleTexture(ItemRegistry.ITEM_PIPE.get(), "item/pipe/item_pipe");
        simpleTexture(ItemRegistry.FLUID_PIPE.get(), "item/pipe/fluid_pipe");
    }

    public void registerIngots() {
        simpleTexture(ItemRegistry.COPPER_INGOT.get(), "item/ingot/copper_ingot");
        simpleTexture(ItemRegistry.ALUMINIUM_INGOT.get(), "item/ingot/aluminium_ingot");
        simpleTexture(ItemRegistry.LEAD_INGOT.get(), "item/ingot/lead_ingot");
        simpleTexture(ItemRegistry.NICKEL_INGOT.get(), "item/ingot/nickel_ingot");

        simpleTexture(ItemRegistry.COPPER_NUGGET.get(), "item/nugget/copper_nugget");
        simpleTexture(ItemRegistry.ALUMINIUM_NUGGET.get(), "item/nugget/aluminium_nugget");
        simpleTexture(ItemRegistry.LEAD_NUGGET.get(), "item/nugget/lead_nugget");
        simpleTexture(ItemRegistry.NICKEL_NUGGET.get(), "item/nugget/nickel_nugget");
    }
}
