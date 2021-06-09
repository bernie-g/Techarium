package software.bernie.techarium.datagen;

import net.minecraft.data.DataGenerator;
import software.bernie.techarium.registry.BlockTileRegistry;
import software.bernie.techarium.registry.ItemGroupRegistry;
import software.bernie.techarium.registry.ItemRegistry;
import software.bernie.techarium.registry.LangRegistry;

public class TechariumLangProvider extends TechariumLangProviderBase {
    public TechariumLangProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addTranslations() {
        addMachines();
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
    }

    protected void addMachines() {
        addBlock(BlockTileRegistry.BOTARIUM, "Botarium");
        addBlock(BlockTileRegistry.BOTARIUM_TOP, "Botarium");
        addBlock(BlockTileRegistry.ARBORETUM, "Arboretum");
        addBlock(BlockTileRegistry.ARBORETUM_TOP, "Arboretum");
    }

    private void addTranslationComponents() {
        addTranslation(LangRegistry.topProgressETA, "ETA: ");
        addTranslation(LangRegistry.hwylaProgressETA, "ETA: %s seconds remaining");
        addTranslation(LangRegistry.hwylaProgressNoRecipe, "ETA: No valid recipe");
        addTranslation(LangRegistry.jeiBotariumDescription, "The Botarium allows you to grow crops in exchange for energy and water");
        addTranslation(LangRegistry.jeiArboretumDescription, "The Arboretum allows you to grow saplings in exchange for energy and water");
    }
}
