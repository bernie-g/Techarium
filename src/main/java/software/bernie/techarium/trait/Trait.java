package software.bernie.techarium.trait;

import software.bernie.techarium.trait.behaviour.Behaviour;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Trait {

    private final Map<Class<?>, Consumer<Object>> tweakers = new HashMap<>();

    /**
     * Applies any tweaks this trait has onto the passed in object
     */
    public void tweak(Object... objects) {
        for (Object object : objects) {
            Consumer<Object> tweaker = tweakers.get(object.getClass());
            if (tweaker != null) {
                tweaker.accept(object);
            }
        }
    }

    /**
     * Gets the first tweaker that is able to take in that object
     */
    public Consumer<Object> getTweaker(Class<?> clazz) {
        return tweakers.entrySet().stream().filter(entry -> entry.getKey().isAssignableFrom(clazz)).findFirst()
                .map(Map.Entry::getValue).orElse(null);
    }

    protected <T> void addTweaker(Class<T> tweakerType, Consumer<T> tweaker) {
        tweakers.put(tweakerType, (Consumer<Object>) tweaker);
    }

    /**
     * Make sure that this behaviour is configured properly for this trait and throw an exception if it isn't.
     *
     * @param behaviour the behaviour
     * @throws Exception the exception
     */
    public void verifyTrait(Behaviour behaviour) throws Exception {

    }

    @Override
    public Trait clone(){
        return this;
    }
}
