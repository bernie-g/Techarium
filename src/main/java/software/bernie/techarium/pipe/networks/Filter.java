package software.bernie.techarium.pipe.networks;

public interface Filter<ToTransport> {

    default boolean canPassThrough(ToTransport toTransport) {
        return true;
    }

}
