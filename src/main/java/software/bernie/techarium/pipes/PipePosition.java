package software.bernie.techarium.pipes;

import lombok.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

@Data
@AllArgsConstructor
public class PipePosition implements INBTSerializable<CompoundNBT> {
    BlockPos pos;
    Direction direction;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PipePosition) {
            return pos.equals(((PipePosition) obj).getPos()) && direction == ((PipePosition)obj).direction;
        } else if (obj instanceof BlockPos) {
            return pos.equals(obj);
        }
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = NBTUtil.writeBlockPos(pos);
        nbt.putInt("direction", direction.get3DDataValue());
        return nbt;
    }

    public static PipePosition createFromNBT(CompoundNBT nbt) {
        PipePosition position = new PipePosition(BlockPos.ZERO, Direction.NORTH);
        position.deserializeNBT(nbt);
        return position;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        pos = NBTUtil.readBlockPos(nbt);
        direction = Direction.from3DDataValue(nbt.getInt("direction"));
    }
}
