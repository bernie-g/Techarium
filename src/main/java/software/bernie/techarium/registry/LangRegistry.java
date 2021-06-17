package software.bernie.techarium.registry;

import software.bernie.techarium.registry.lang.LangEntry;
import software.bernie.techarium.registry.lang.TranslationLangEntry;

import java.util.ArrayList;
import java.util.List;

import static software.bernie.techarium.registry.lang.LangRegistryBase.register;
import static software.bernie.techarium.registry.lang.LangRegistryBase.registerDescription;

public class LangRegistry {
    public static final List<LangEntry<?, ?>> langEntries = new ArrayList<>();

    public static final TranslationLangEntry topProgressETA = register("top", "progress.eta");
    public static final TranslationLangEntry hwylaProgressETA = register("hwyla", "progress.eta");
    public static final TranslationLangEntry hwylaProgressNoRecipe = register("hwyla", "progress.no_recipe");
    public static final TranslationLangEntry jeiBotariumDescription = register("jei", "botarium.description");
    public static final TranslationLangEntry jeiArboretumDescription = register("jei", "arboretum.description");
    public static final TranslationLangEntry guiPipeInput = register("gui", "pipe.input");
    public static final TranslationLangEntry guiPipeOutput = register("gui", "pipe.output");
    public static final TranslationLangEntry guiPipeRoundRobin = register("gui", "pipe.round_robin");
    public static final TranslationLangEntry guiPipeSelfFeed = register("gui", "pipe.self_feed");

    public static final TranslationLangEntry botariumDescription = registerDescription(BlockRegistry.BOTARIUM.getBlockRegistryObject());
}
