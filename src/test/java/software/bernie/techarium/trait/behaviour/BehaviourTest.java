package software.bernie.techarium.trait.behaviour;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.trait.block.BlockTraits;
import software.bernie.techarium.util.ClassMap;

import java.util.Optional;

class BehaviourTest {

    @Test
    void testClassMap() {
        ClassMap<Trait> traits = new ClassMap<>(Trait.class);
        traits.add(new BlockTraits.StrengthTrait());
        Optional<BlockTraits.MaterialTrait> trait = traits.findFirst(BlockTraits.MaterialTrait.class);
        Assertions.assertNotNull(trait.get());
    }
}