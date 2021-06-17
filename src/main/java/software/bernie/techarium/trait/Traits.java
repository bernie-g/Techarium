package software.bernie.techarium.trait;

import lombok.AllArgsConstructor;
import software.bernie.techarium.registry.lang.LangEntry;

public class Traits {

    @AllArgsConstructor
    public static class DescriptionTrait extends Trait {
        public final LangEntry description;
    }
}
