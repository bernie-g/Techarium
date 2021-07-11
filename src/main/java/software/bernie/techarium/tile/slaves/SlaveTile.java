package software.bernie.techarium.tile.slaves;

import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.tile.base.MachineSlaveTile;

import java.util.EnumMap;
import java.util.Map;

import static software.bernie.techarium.registry.BlockRegistry.BOTARIUM_TOP_TILE;

public class SlaveTile extends MachineSlaveTile {

    public SlaveTile() {
        super(BOTARIUM_TOP_TILE.get());
    }

    @Override
    protected Map<Side, FaceConfig> setFaceControl() {
        Map<Side, FaceConfig> topMap = new EnumMap<>(Side.class);
        for(Side side: Side.values()){
            if(side != Side.UP) {
                topMap.put(side, FaceConfig.NONE);
            } else{
                topMap.put(side, FaceConfig.ENABLED);
            }
        }
        return topMap;
    }
}
