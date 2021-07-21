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
        addDescriptions();
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

        addItem(ItemRegistry.SURVIVALIST_EXOSUIT_HELMET, "Survivalist Exosuit Helmet");
        addItem(ItemRegistry.SURVIVALIST_EXOSUIT_CHESTPLATE, "Survivalist Exosuit Chestplate");
        addItem(ItemRegistry.SURVIVALIST_EXOSUIT_LEGGINGS, "Survivalist Exosuit Leggings");
        addItem(ItemRegistry.SURVIVALIST_EXOSUIT_BOOTS, "Survivalist Exosuit Boots");
    }

    protected void addBlocks() {
        addBlock(BlockRegistry.BOTARIUM, "Botarium");
        addBlock(BlockRegistry.BOTARIUM_TOP, "Botarium");
        addBlock(BlockRegistry.ARBORETUM, "Arboretum");
        addBlock(BlockRegistry.ARBORETUM_TOP, "Arboretum");

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

        addTranslation(LangRegistry.guiPipeInput, "Input");
        addTranslation(LangRegistry.guiPipeOutput, "Output");
        addTranslation(LangRegistry.guiPipeRoundRobin, "Round-Robin");
        addTranslation(LangRegistry.guiPipeSelfFeed, "Self Feed");
    }

    private void addDescriptions() {
        addDescription(LangRegistry.botariumDescription, "The Botarium allows you to grow crops in exchange for energy and a suitable fluid");
        addDescription(LangRegistry.arboretumDescription, "The Arboretum allows you to grow saplings in exchange for energy and water");
        addDescription(LangRegistry.exchangeDescription, "The Exchange Station allows you to unlock new machines with gold");
        addDescription(LangRegistry.machineShiftDescription, "&6Hold &b[LShift] &6for a description");
        addDescription(LangRegistry.survivalistExosuitDescription, "The survivalist exosuit is an early-game powersuit that gives haste and speed when wearing a full set.");
    }
}
