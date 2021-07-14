package software.bernie.techarium.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import software.bernie.techarium.datagen.base.TechariumBlockStateProviderBase;
import software.bernie.techarium.registry.BlockRegistry;

public class TechariumBlockStateProvider extends TechariumBlockStateProviderBase {
    public TechariumBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerMachines();
        registerOres();
        registerBlock();
    }

    public void registerMachines(){
        simpleMachineBox(BlockRegistry.BOTARIUM, models().modLoc("block/botarium_side"));
        simpleMachineBox(BlockRegistry.ARBORETUM, models().modLoc("block/botarium_side"));
        simpleMachineXYZBox(BlockRegistry.GRAVMAGNET, models().modLoc("block/gravmagnet_side"));
        simpleMachineXYZBox(BlockRegistry.MAGNETIC_COIL, models().modLoc("block/magnetic_coil_side"));
        simpleMachineBox(BlockRegistry.VOLTAIC_PILE, models().modLoc("block/botarium_side"));

        invisMachine(BlockRegistry.BOTARIUM_TOP.get());
        invisMachine(BlockRegistry.ARBORETUM_TOP.get());
        invisMachine(BlockRegistry.EXCHANGE_STATION.getBlock());
        invisBlock(BlockRegistry.PIPE.getBlock());
    }

    public void registerOres(){
        simpleBlockAndItem(BlockRegistry.COPPER_ORE.get());
        simpleBlockAndItem(BlockRegistry.ALUMINIUM_ORE.get());
        simpleBlockAndItem(BlockRegistry.LEAD_ORE.get());
        simpleBlockAndItem(BlockRegistry.NICKEL_ORE.get());
        simpleBlockAndItem(BlockRegistry.ZINC_ORE.get());
    }
    
    public void registerBlock(){
        simpleBlockAndItem(BlockRegistry.COPPER_BLOCK.get());
        simpleBlockAndItem(BlockRegistry.ALUMINIUM_BLOCK.get());
        simpleBlockAndItem(BlockRegistry.LEAD_BLOCK.get());
        simpleBlockAndItem(BlockRegistry.NICKEL_BLOCK.get());
    }
}
