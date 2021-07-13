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

        addItem(ItemRegistry.ALUMINIUM_INGOT, "Aluminium Ingot");
        addItem(ItemRegistry.COPPER_INGOT, "Copper Ingot");
        addItem(ItemRegistry.LEAD_INGOT, "Lead Ingot");
        addItem(ItemRegistry.NICKEL_INGOT, "Nickel Ingot");

        addItem(ItemRegistry.ALUMINIUM_NUGGET, "Aluminium Nugget");
        addItem(ItemRegistry.COPPER_NUGGET, "Copper Nugget");
        addItem(ItemRegistry.LEAD_NUGGET, "Lead Nugget");
        addItem(ItemRegistry.NICKEL_NUGGET, "Nickel Nugget");
    }

    protected void addBlocks() {
        addBlock(BlockRegistry.BOTARIUM, "Botarium");
        addBlock(BlockRegistry.ARBORETUM, "Arboretum");

        addBlock(BlockRegistry.EXCHANGE_STATION, "Exchange Station");

        addBlock(BlockRegistry.ALUMINIUM_ORE, "Aluminium Ore");
        addBlock(BlockRegistry.COPPER_ORE, "Copper Ore");
        addBlock(BlockRegistry.LEAD_ORE, "Lead Ore");
        addBlock(BlockRegistry.NICKEL_ORE, "Nickel Ore");

        addBlock(BlockRegistry.NICKEL_BLOCK, "Nickel Block");
        addBlock(BlockRegistry.ALUMINIUM_BLOCK, "Aluminum Block");
        addBlock(BlockRegistry.COPPER_BLOCK, "Copper Block");
        addBlock(BlockRegistry.LEAD_BLOCK, "Lead Block");
    }

    private void addTranslationComponents() {
        addTranslation(LangRegistry.topProgressETA, "ETA: ");
        addTranslation(LangRegistry.hwylaProgressETA, "ETA: %s seconds remaining");
        addTranslation(LangRegistry.hwylaProgressNoRecipe, "ETA: No valid recipe");
        addTranslation(LangRegistry.botariumDescription, "The Botarium allows you to grow crops in exchange for energy and a suitable fluid");
        addTranslation(LangRegistry.arboretumDescription, "The Arboretum allows you to grow saplings in exchange for energy and water");
        addTranslation(LangRegistry.exchangeStationDescription, "The Exchange Station allows you to unlock new machines with gold");
        addTranslation(LangRegistry.guiPipeInput, "Input");
        addTranslation(LangRegistry.guiPipeOutput, "Output");
        addTranslation(LangRegistry.guiPipeRoundRobin, "Round-Robin");
        addTranslation(LangRegistry.guiPipeSelfFeed, "Self Feed");
        addTranslation(LangRegistry.machineShiftDescription, "Hold [LShift] for description");


    }
}
