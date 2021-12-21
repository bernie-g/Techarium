package software.bernie.techarium.trait.item;

import software.bernie.techarium.trait.behaviour.PartialBehaviour;

public class ItemPartialBehaviours {
    public static PartialBehaviour partialBaseItem = new ItemBehaviour.Builder()
            .partial();
}
