package software.bernie.techarium.pipes;

import lombok.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

@Data
@AllArgsConstructor
public class PipePosition {
    BlockPos pos;
    Direction direction;
}
