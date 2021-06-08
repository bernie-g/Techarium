package software.bernie.techarium.registry.lang;

public class LangEntryImpl<T> extends LangEntry<T, LangEntryImpl<T>> {

    private T object;

    public LangEntryImpl(T object, String description) {
        super(description);
        this.object = object;
    }

    @Override
    public T get() {
        return object;
    }
}
