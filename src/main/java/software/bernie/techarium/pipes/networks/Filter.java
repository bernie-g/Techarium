package software.bernie.techarium.pipes.networks;

public interface Filter<ToTransport> {

    default boolean canPassThrough(ToTransport toTransport) {
        return true;
    }

}
