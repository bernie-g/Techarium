package software.bernie.techarium.block.pipe;

import lombok.Data;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

import java.util.EnumMap;
import java.util.Map;

@Data
public class PipeData {
    private static final int MAX_NUM_PIPES = 4;

    public final Map<Direction, Boolean> pipeEnds = new EnumMap<>(Direction.class);
    public final Map<Direction, Integer> pipeConnections = new EnumMap<>(Direction.class);
    public int[] types = new int[MAX_NUM_PIPES];

    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT endNBT = new ListNBT();
        for (Map.Entry<Direction, Boolean> end: pipeEnds.entrySet()) {
            CompoundNBT endDataNBT = new CompoundNBT();
            endDataNBT.putInt("direction", end.getKey().get3DDataValue());
            endDataNBT.putBoolean("isend", end.getValue());
            endNBT.add(endDataNBT);
        }
        nbt.put("enddata", endNBT);
        ListNBT connectionNBT = new ListNBT();
        for (Map.Entry<Direction, Integer> connection: pipeConnections.entrySet()) {
            CompoundNBT connectionDataNBT = new CompoundNBT();
            connectionDataNBT.putInt("direction", connection.getKey().get3DDataValue());
            connectionDataNBT.putInt("connection", connection.getValue());
            connectionNBT.add(connectionDataNBT);
        }
        nbt.put("connection", connectionNBT);
        nbt.put("types", new IntArrayNBT(types));
        return nbt;
    }

    public static PipeData deserialize(CompoundNBT nbt) {
        PipeData data = new PipeData();
        for(INBT endNBT: nbt.getList("enddata", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT compEndNBT = (CompoundNBT) endNBT;
            data.pipeEnds.put(Direction.from3DDataValue(compEndNBT.getInt("direction")), compEndNBT.getBoolean("isend"));
        }
        for(INBT endNBT: nbt.getList("connection", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT compEndNBT = (CompoundNBT) endNBT;
            data.pipeConnections.put(Direction.from3DDataValue(compEndNBT.getInt("direction")), compEndNBT.getInt("connection"));
        }
        data.types = nbt.getIntArray("types");
        return data;
    }
}
