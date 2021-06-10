package software.bernie.techarium.tile.slaves;

import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.tile.base.MachineSlaveTile;

import java.util.HashMap;
import java.util.Map;

import static software.bernie.techarium.registry.BlockRegistry.BOTARIUM_TOP_TILE;

public class TopEnabledOnlySlave extends MachineSlaveTile {

    public TopEnabledOnlySlave() {
        super(BOTARIUM_TOP_TILE.get());
    }

    @Override
    protected Map<Side, FaceConfig> setFaceControl() {
        Map<Side, FaceConfig> topMap = new HashMap<>();
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
