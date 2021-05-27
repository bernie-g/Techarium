package software.bernie.techarium.pipes.networks;

public class Filter<ToTransport> {

    public boolean canPassThrough(ToTransport toTransport) {
        return true;
    }

}
