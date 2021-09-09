package software.bernie.techarium.trait.item;

import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.trait.behaviour.Behaviour;

public class ItemBehaviour extends Behaviour {
    public static class Builder extends Behaviour.Builder<ItemBehaviour, ItemBehaviour.Builder> {
        public Builder() {
            super(new ItemBehaviour());
        }
    }

    public ItemBehaviour copy() {
        ItemBehaviour.Builder builder = new ItemBehaviour.Builder();
        for (Trait trait : this.traits.values()) {
            try {
                builder = builder.with((Trait) trait.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }
}
