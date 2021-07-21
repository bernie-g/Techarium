package software.bernie.techarium.trait.item;

import software.bernie.techarium.trait.Traits;
import software.bernie.techarium.trait.behaviour.PartialBehaviour;

public class ItemPartialBehaviours {
    public static final PartialBehaviour baseItemBehaviour = new ItemBehaviour.Builder().requiredTraits(Traits.DescriptionTrait.class).partial();
}
