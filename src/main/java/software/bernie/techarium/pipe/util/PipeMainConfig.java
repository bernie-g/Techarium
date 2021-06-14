package software.bernie.techarium.pipe.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;


@Accessors(chain = true)
@Getter
@Setter
public class PipeMainConfig implements INBTSerializable<CompoundNBT> {


    private boolean isInput = true;
    private boolean isOutput = true;
    private boolean roundRobin = false;
    private boolean selfFeed = true;

    public PipeMainConfig clone() {
        PipeMainConfig config = new PipeMainConfig();
        config.isInput = isInput;
        config.isOutput = isOutput;
        config.roundRobin = roundRobin;
        config.selfFeed = selfFeed;
        return config;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putBoolean("isInput", isInput);
        nbt.putBoolean("isOutput", isOutput);
        nbt.putBoolean("roundRobin", roundRobin);
        nbt.putBoolean("selfFeed", selfFeed);

        return nbt;
    }

    public static PipeMainConfig of(CompoundNBT nbt) {
        PipeMainConfig config = new PipeMainConfig();
        config.deserializeNBT(nbt);
        return config;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        isInput = nbt.getBoolean("isInput");
        isOutput = nbt.getBoolean("isOutput");
        roundRobin = nbt.getBoolean("roundRobin");
        selfFeed = nbt.getBoolean("selfFeed");
    }
}
