package software.bernie.techarium.machine.addon;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum ExposeType {
    INPUT(true, false),
    OUTPUT(false, true),
    BOTH(true, true),
    NONE(false, false);

    private boolean canInsert;
    private boolean canExtract;
    ExposeType(boolean canInsert, boolean canExtract) {
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }
}
