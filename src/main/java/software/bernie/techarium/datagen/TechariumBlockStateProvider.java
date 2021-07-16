package software.bernie.techarium.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import software.bernie.techarium.block.voltaicpile.Charge;
import software.bernie.techarium.block.voltaicpile.VoltaicPileBlock;
import software.bernie.techarium.datagen.base.TechariumBlockStateProviderBase;
import software.bernie.techarium.registry.BlockRegistry;

public class TechariumBlockStateProvider extends TechariumBlockStateProviderBase {
    public TechariumBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, exFileHelper);
    }

    public static final String BLOCK_DIR = ModelProvider.BLOCK_FOLDER;

    @Override
    protected void registerStatesAndModels() {
        registerMachines();
        registerOres();
        registerVoltaicPile();
    }

    public void registerMachines(){
        simpleMachineBox(BlockRegistry.BOTARIUM, models().modLoc("block/botarium_side"));
        simpleMachineBox(BlockRegistry.ARBORETUM, models().modLoc("block/botarium_side"));

        invisMachine(BlockRegistry.BOTARIUM_TOP.get());
        invisMachine(BlockRegistry.ARBORETUM_TOP.get());
        invisBlock(BlockRegistry.PIPE.getBlock());
    }

    public void registerOres(){
        simpleBlockAndItem(BlockRegistry.COPPER_ORE.get());
        simpleBlockAndItem(BlockRegistry.ALUMINIUM_ORE.get());
        simpleBlockAndItem(BlockRegistry.LEAD_ORE.get());
        simpleBlockAndItem(BlockRegistry.NICKEL_ORE.get());
        simpleBlockAndItem(BlockRegistry.ZINC_ORE.get());
    }

    private void registerVoltaicPile(){
        Block pileBlock = BlockRegistry.VOLTAIC_PILE.getBlock();
        String pileSegmentPath = BLOCK_DIR + "/" + pileBlock.getRegistryName().getPath();
        VariantBlockStateBuilder multipart = getVariantBuilder(pileBlock);

        for (Charge e : VoltaicPileBlock.CHARGE.getPossibleValues()) {
            String add = e == Charge.EMPTY ? "_empty" : e == Charge.ONE_THIRD ? "_33" : e == Charge.TWO_THIRD ? "_66" : "_full";
            ModelFile.ExistingModelFile basemodel = models().getExistingFile(modLoc(pileSegmentPath + add));
            for (Direction r : VoltaicPileBlock.HORIZONTAL_FACING.getPossibleValues()) {
                int rotation = r == Direction.WEST ? 90 : r == Direction.NORTH ? 180 : r == Direction.EAST ? 270 : 0;
                multipart.partialState()
                        .with(VoltaicPileBlock.CHARGE, e)
                        .with(VoltaicPileBlock.HORIZONTAL_FACING, r)
                        .modelForState()
                        .modelFile(basemodel)
                        .rotationY(rotation)
                        .addModel();
            }
        }

        this.simpleBlockItem(pileBlock, models().getExistingFile(modLoc(pileSegmentPath + "_full")));
    }
}
