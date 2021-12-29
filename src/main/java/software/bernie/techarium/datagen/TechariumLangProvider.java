package software.bernie.techarium.datagen;

import net.minecraft.data.DataGenerator;
import software.bernie.techarium.datagen.base.TechariumLangProviderBase;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.registry.ItemGroupRegistry;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.registry.LangRegistry;

public class TechariumLangProvider extends TechariumLangProviderBase {
    public TechariumLangProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addTranslations() {
        addBlocks();
        addItems();
        addTranslationComponents();
        addItemGroups();
    }

    private void addItemGroups() {
        add(ItemGroupRegistry.TECHARIUM, "Techarium");
    }

    private void addItems() {
        addItem(ItemRegistry.DEBUGSTICK, "Pipe Stick");
        addItem(ItemRegistry.ITEM_PIPE, "Item Pipe");
        addItem(ItemRegistry.FLUID_PIPE, "Fluid Pipe");
        addItem(ItemRegistry.ENERGY_PIPE, "Energy Pipe");
        addItem(ItemRegistry.HAMMER, "Hammer");

        addItem(ItemRegistry.ALUMINIUM_INGOT, "Aluminium Ingot");
        addItem(ItemRegistry.COPPER_INGOT, "Copper Ingot");
        addItem(ItemRegistry.LEAD_INGOT, "Lead Ingot");
        addItem(ItemRegistry.NICKEL_INGOT, "Nickel Ingot");
        addItem(ItemRegistry.ZINC_INGOT, "Zinc Ingot");

        addItem(ItemRegistry.ALUMINIUM_PLATE, "Aluminium Plate");
        addItem(ItemRegistry.COPPER_PLATE, "Copper Plate");
        addItem(ItemRegistry.LEAD_PLATE, "Lead Plate");
        addItem(ItemRegistry.NICKEL_PLATE, "Nickel Plate");
        addItem(ItemRegistry.ZINC_PLATE, "Zinc Plate");

        addItem(ItemRegistry.ALUMINIUM_NUGGET, "Aluminium Nugget");
        addItem(ItemRegistry.COPPER_NUGGET, "Copper Nugget");
        addItem(ItemRegistry.LEAD_NUGGET, "Lead Nugget");
        addItem(ItemRegistry.NICKEL_NUGGET, "Nickel Nugget");
        addItem(ItemRegistry.ZINC_NUGGET, "Zinc Nugget");
    }

    protected void addBlocks() {
        addBlock(BlockRegistry.BOTARIUM, "Botarium");
        addBlock(BlockRegistry.BOTARIUM_TOP, "Botarium");
        addBlock(BlockRegistry.ARBORETUM, "Arboretum");
        addBlock(BlockRegistry.ARBORETUM_TOP, "Arboretum");
        addBlock(BlockRegistry.BEAM, "Metal Beam");

        addBlock(BlockRegistry.DEPOT, "Depot");
        addBlock(BlockRegistry.GRAVMAGNET, "Grav Magnet");
        addBlock(BlockRegistry.MAGNETIC_COIL, "Magnetic Coil");

        addBlock(BlockRegistry.VOLTAIC_PILE, "Voltaic Pile");
        addBlock(BlockRegistry.EXCHANGE_STATION, "Exchange Station");

        addBlock(BlockRegistry.ALUMINIUM_ORE, "Aluminium Ore");
        addBlock(BlockRegistry.COPPER_ORE, "Copper Ore");
        addBlock(BlockRegistry.LEAD_ORE, "Lead Ore");
        addBlock(BlockRegistry.NICKEL_ORE, "Nickel Ore");
        addBlock(BlockRegistry.ZINC_ORE, "Zinc Ore");

        addBlock(BlockRegistry.NICKEL_BLOCK, "Block of Nickel");
        addBlock(BlockRegistry.ALUMINIUM_BLOCK, "Block of Aluminum");
        addBlock(BlockRegistry.COPPER_BLOCK, "Block of Copper");
        addBlock(BlockRegistry.LEAD_BLOCK, "Block of Lead");
        addBlock(BlockRegistry.ZINC_BLOCK, "Block of Zinc");

        addBlock(BlockRegistry.NICKEL_PLATE_BLOCK, "Block of Nickel Plates");
        addBlock(BlockRegistry.ALUMINIUM_PLATE_BLOCK, "Block of Aluminum Plates");
        addBlock(BlockRegistry.COPPER_PLATE_BLOCK, "Block of Copper Plates");
        addBlock(BlockRegistry.LEAD_PLATE_BLOCK, "Block of Lead Plates");
        addBlock(BlockRegistry.ZINC_PLATE_BLOCK, "Block of Zinc Plates");

        addBlock(BlockRegistry.ENCASED_NICKEL_BLOCK, "Encased Nickel Block");
        addBlock(BlockRegistry.ENCASED_ALUMINUM_BLOCK, "Encased Aluminium Block");
        addBlock(BlockRegistry.ENCASED_COPPER_BLOCK, "Encased Copper Block");
        addBlock(BlockRegistry.ENCASED_LEAD_BLOCK, "Encased Lead Block");
        addBlock(BlockRegistry.ENCASED_ZINC_BLOCK, "Encased Zinc Block");

        addBlock(BlockRegistry.ALUMINIUM_FACTORY_BLOCK, "Aluminium Factory Block");
        addBlock(BlockRegistry.COPPER_FACTORY_BLOCK, "Copper Factory Block");
        addBlock(BlockRegistry.NICKEL_FACTORY_BLOCK, "Nickel Factory Block");
        addBlock(BlockRegistry.LEAD_FACTORY_BLOCK, "Lead Factory Block");
        addBlock(BlockRegistry.ZINC_FACTORY_BLOCK, "Zinc Factory Block");

        addBlock(BlockRegistry.TECH_BUTTON, "Button");
        addBlock(BlockRegistry.TECH_LEVER, "Lever");
        
        addBlock(BlockRegistry.ALUMINIUM_LADDER, "Aluminium Ladder");
    }

    private void addTranslationComponents() {
        addTranslation(LangRegistry.topProgressETA, "ETA: ");
        addTranslation(LangRegistry.hwylaProgressETA, "ETA: %s seconds remaining");
        addTranslation(LangRegistry.hwylaProgressNoRecipe, "ETA: No valid recipe");
        addTranslation(LangRegistry.botariumDescription, "The Botarium allows you to grow crops in exchange for energy and a suitable fluid");
        addTranslation(LangRegistry.arboretumDescription, "The Arboretum allows you to grow saplings in exchange for energy and water");
        addTranslation(LangRegistry.exchangeDescription, "The Exchange Station allows you to unlock new machines with gold");
        addTranslation(LangRegistry.voltaicPileDescription, "The Voltaic Pile is a single-use, portable medium capacity energy storage with low RF/t output.");
        addTranslation(LangRegistry.guiPipeInput, "Input");
        addTranslation(LangRegistry.guiPipeOutput, "Output");
        addTranslation(LangRegistry.guiPipeRoundRobin, "Round-Robin");
        addTranslation(LangRegistry.guiPipeSelfFeed, "Self Feed");
        addTranslation(LangRegistry.machineShiftDescription, "Hold [LShift] for description");
        addTranslation(LangRegistry.gravMagnetDescription, "The Grav Magnet will attract or repel every entity in front of the block. Use a redstone signal to switch between the two modes. Can be boosted if a magnetic coil is placed behind");
        addTranslation(LangRegistry.magneticCoilDescription, "The Magnetic Coil is used to boost the Grav Magnet. Add a coil inside to boost the power");
        addTranslation(LangRegistry.depotDescription, "The Depot is used to create Plates and automate the GravMagnet Recipes.");
    }
}
