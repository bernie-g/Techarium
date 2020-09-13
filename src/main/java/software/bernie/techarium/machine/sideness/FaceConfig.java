package software.bernie.techarium.machine.sideness;

public enum FaceConfig {
    NONE(false,0),
    ENABLED(true,1),
    PUSH_ONLY(true,2),
    PULL_ONLY(true,3);

    private final boolean allowsConnection;
    private final int index;


    FaceConfig(boolean allowsConnection, int index) {
        this.allowsConnection = allowsConnection;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean allowsConnection() {
        return allowsConnection;
    }
}
