package software.bernie.techarium.block.voltaicpile;

import net.minecraft.util.IStringSerializable;

public enum Charge implements IStringSerializable {
    EMPTY("empty"),
    ONE_THIRD("one_third"),
    TWO_THIRD("two_third"),
    FULL("full");

    private final String name;

    Charge(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}