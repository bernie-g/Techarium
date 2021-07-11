package software.bernie.techarium.util;


import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class ShapeUtilsTest {

    public static class ShapeTest {
        public final VoxelShape voxelShape;
        public final int expectedSlaves;

        public ShapeTest(int expectedSlaves, VoxelShape voxelShape) {
            this.voxelShape = voxelShape;
            this.expectedSlaves = expectedSlaves;
        }
    }


    public static Stream<Arguments> getVoxelTests() {
        return Stream.of(
                Arguments.of(
                        new ShapeTest(8,
                                VoxelShapes.or(
                                        VoxelShapes.box(0, 0, 0, 2, 2, 2),
                                        VoxelShapes.box(-2, -2, -2, -1, -1, -1)
                                )))
        );
    }


    @ParameterizedTest
    @MethodSource(value = "getVoxelTests")
    public void testSlaves1(ShapeTest test) {
        List<SlaveSpot> slaveOffsets = ShapeUtils.getSlaveOffsets(test.voxelShape);
        Assertions.assertEquals(test.expectedSlaves, slaveOffsets.size());
    }

}