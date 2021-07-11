package software.bernie.techarium.trait.behaviour;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.bernie.techarium.trait.Trait;
import software.bernie.techarium.trait.block.BlockTraits;
import software.bernie.techarium.util.ClassInheritanceMap;

import java.util.Optional;

class BehaviourTest {

    @Test
    void testClassMap() {
        ClassInheritanceMap<Trait> traits = new ClassInheritanceMap<>(Trait.class);
        traits.put(new BlockTraits.MaterialTrait());
        traits.put(new Trait());
        traits.put(new BlockTraits.ParticlesTrait(true));
        traits.put(new BlockTraits.StrengthTrait());
        traits.put(new BlockTraits.StrengthTrait());
        Optional<BlockTraits.MaterialTrait> trait = traits.getOptional(BlockTraits.MaterialTrait.class);

        Assertions.assertNotNull(trait.get());
    }
}