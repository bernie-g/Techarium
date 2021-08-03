package software.bernie.techarium.trait.item;

import software.bernie.techarium.registry.LangRegistry;

public class ItemBehaviours {
    public static final ItemBehaviour survivalistExosuitBehaviour = new ItemBehaviour.Builder()
            .composeFrom(ItemPartialBehaviours.baseItemBehaviour)
            .description(LangRegistry.survivalistExosuitDescription)
            .build();
}
