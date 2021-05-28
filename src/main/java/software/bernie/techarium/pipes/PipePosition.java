package software.bernie.techarium.pipes;

import lombok.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

@Data
@AllArgsConstructor
public class PipePosition {
    BlockPos pos;
    Direction direction;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PipePosition) {
            return pos.equals(((PipePosition) obj).getPos()) && direction == ((PipePosition)obj).direction;
        } else if (obj instanceof BlockPos) {
            return pos.equals((BlockPos)obj);
        }
        return false;
    }
}
