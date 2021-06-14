package software.bernie.techarium.pipe.util;

import lombok.Getter;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class PipeConfig implements INBTSerializable<CompoundNBT> {


    private Map<Direction, PipeUsableConfig> inputUsableConfig = new EnumMap<>(Direction.class);

    private Map<Direction, PipeUsableConfig> outputUsableConfig = new EnumMap<>(Direction.class);

    private Map<Direction, PipeMainConfig> mainConfig = new EnumMap<>(Direction.class);

    public PipeConfig() {
        for (Direction direction : Direction.values()) {
            inputUsableConfig.put(direction, new PipeUsableConfig());
            outputUsableConfig.put(direction, new PipeUsableConfig());
            mainConfig.put(direction, new PipeMainConfig());
        }
    }

    public Map<Direction, PipeUsableConfig> getConfigBy(boolean isInput) {
        return isInput ? inputUsableConfig : outputUsableConfig;
    }

    public static PipeConfig of(CompoundNBT nbt) {
        PipeConfig config = new PipeConfig();
        config.deserializeNBT(nbt);
        return config;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT mapEntries = new ListNBT();
        for (Map.Entry<Direction, PipeUsableConfig> entry: inputUsableConfig.entrySet()) {
            CompoundNBT tempNBT = new CompoundNBT();
            tempNBT.putInt("direction", entry.getKey().ordinal());
            tempNBT.put("usable", entry.getValue().serializeNBT());
            mapEntries.add(tempNBT);
        }
        nbt.put("input", mapEntries);
        mapEntries = new ListNBT();
        for (Map.Entry<Direction, PipeUsableConfig> entry: outputUsableConfig.entrySet()) {
            CompoundNBT tempNBT = new CompoundNBT();
            tempNBT.putInt("direction", entry.getKey().ordinal());
            tempNBT.put("usable", entry.getValue().serializeNBT());
            mapEntries.add(tempNBT);
        }
        nbt.put("output", mapEntries);
        mapEntries = new ListNBT();
        for (Map.Entry<Direction, PipeMainConfig> entry: mainConfig.entrySet()) {
            CompoundNBT tempNBT = new CompoundNBT();
            tempNBT.putInt("direction", entry.getKey().ordinal());
            tempNBT.put("usable", entry.getValue().serializeNBT());
            mapEntries.add(tempNBT);
        }
        nbt.put("main", mapEntries);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT mapEntries = nbt.getList("input", Constants.NBT.TAG_COMPOUND);
        for (INBT inbt: mapEntries) {
            CompoundNBT tempNBT = (CompoundNBT) inbt;
            inputUsableConfig.put(Direction.values()[tempNBT.getInt("direction")], PipeUsableConfig.of((CompoundNBT)tempNBT.get("usable")));
        }
        mapEntries = nbt.getList("output", Constants.NBT.TAG_COMPOUND);
        for (INBT inbt: mapEntries) {
            CompoundNBT tempNBT = (CompoundNBT) inbt;
            outputUsableConfig.put(Direction.values()[tempNBT.getInt("direction")], PipeUsableConfig.of((CompoundNBT)tempNBT.get("usable")));
        }
        mapEntries = nbt.getList("main", Constants.NBT.TAG_COMPOUND);
        for (INBT inbt: mapEntries) {
            CompoundNBT tempNBT = (CompoundNBT) inbt;
            mainConfig.put(Direction.values()[tempNBT.getInt("direction")], PipeMainConfig.of((CompoundNBT)tempNBT.get("usable")));
        }
    }
}
