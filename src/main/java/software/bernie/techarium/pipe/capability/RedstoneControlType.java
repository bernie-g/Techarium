package software.bernie.techarium.pipe.capability;

import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
public enum RedstoneControlType {
    ALWAYS_DISABLED(redstone -> false),
    ALWAYS_ENABLED(redstone -> true),
    ACTIVE_WITH_REDSTONE( redstone -> redstone),
    ACTIVE_WITHOUT_REDSTONE( redstone -> !redstone);

    Function<Boolean, Boolean> isActive;
}
