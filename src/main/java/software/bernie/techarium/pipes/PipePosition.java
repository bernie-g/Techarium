package software.bernie.techarium.pipes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PipePosition {
    BlockPos pos;
    Direction direction;
}
