package software.bernie.techarium.trait.item;

import software.bernie.techarium.registry.lang.LangEntry;
import software.bernie.techarium.trait.Traits;
import software.bernie.techarium.trait.behaviour.Behaviour;

public class ItemBehaviour extends Behaviour {
    public static class Builder extends Behaviour.Builder<ItemBehaviour, ItemBehaviour.Builder> {
        public Builder() {
            super(new ItemBehaviour());
        }

        public ItemBehaviour.Builder description(LangEntry description) {
            return this.with(new Traits.DescriptionTrait(description));
        }
    }
}