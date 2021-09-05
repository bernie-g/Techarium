package software.bernie.techarium.trait;

public interface ICanCopy {
    default public Object deepCopy() {
        return this;
    }
}
