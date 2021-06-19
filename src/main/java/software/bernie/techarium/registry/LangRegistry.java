package software.bernie.techarium.registry;

import software.bernie.techarium.registry.lang.LangEntry;
import software.bernie.techarium.registry.lang.LangRegistryBase;
import software.bernie.techarium.registry.lang.TranslationLangEntry;
import java.util.ArrayList;
import java.util.List;

public class LangRegistry extends LangRegistryBase {
    public static final List<LangEntry<?>> langEntries = new ArrayList<>();

    public static final TranslationLangEntry topProgressETA = LangRegistryBase.register("top", "progress.eta");
    public static final TranslationLangEntry hwylaProgressETA = LangRegistryBase.register("hwyla", "progress.eta");
    public static final TranslationLangEntry hwylaProgressNoRecipe = LangRegistryBase.register("hwyla", "progress.no_recipe");
    public static final TranslationLangEntry guiPipeInput = LangRegistryBase.register("gui", "pipe.input");
    public static final TranslationLangEntry guiPipeOutput = LangRegistryBase.register("gui", "pipe.output");
    public static final TranslationLangEntry guiPipeRoundRobin = LangRegistryBase.register("gui", "pipe.round_robin");
    public static final TranslationLangEntry guiPipeSelfFeed = LangRegistryBase.register("gui", "pipe.self_feed");
    public static final TranslationLangEntry machineShiftDescription = LangRegistryBase.register("shift", "description");
    public static final TranslationLangEntry botariumDescription = registerDescription(BlockRegistry.BOTARIUM.getBlockRegistryObject());
    public static final TranslationLangEntry arboretumDescription = registerDescription(BlockRegistry.ARBORETUM.getBlockRegistryObject());
    public static final TranslationLangEntry exchangeStationDescription = registerDescription(BlockRegistry.EXCHANGE_STATION.getBlockRegistryObject());

}
