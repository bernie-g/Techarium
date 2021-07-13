package software.bernie.techarium.tile.slaves;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.RegistryObject;
import software.bernie.techarium.block.base.TechariumBlock;
import software.bernie.techarium.block.slave.SlaveBlock;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.tile.base.MachineSlaveTile;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class SlaveTile extends MachineSlaveTile {

    private static HashMap<String, RegistryObject<? extends Block>> masters = new HashMap<>();
    private static HashMap<String, RegistryObject<TileEntityType<?>>> slaveTiles = new HashMap<>();
    private static HashMap<String, RegistryObject<SlaveBlock>> slaveBlocks = new HashMap<>();

    @Getter
    private final String masterName;

    public SlaveTile(String name) {
        super(getType(name));
        this.masterName = name;
    }

    public static TileEntityType<?> getType(String name) {
        return slaveTiles.get(name).get();
    }

    /**
     * Registers slave blocks and tile entities for a master block
     *
     * @param name        the name of the master block
     */
    public static void registerMaster(String name, RegistryObject<TechariumBlock> masterBlock) {
        masters.put(name, masterBlock);
        slaveBlocks.put(name, BlockRegistry.BLOCKS
                .register("slave_" + name, () -> new SlaveBlock(masterBlock, masterBlock.get().getBehaviour())));
        slaveTiles.put(name, BlockRegistry.TILES
                .register("slave_" + name, () -> TileEntityType.Builder.of(() -> new SlaveTile(name), masterBlock.get())
                        .build(null)));
    }

    public static RegistryObject<SlaveBlock> getSlaveBlock(String name){
        return slaveBlocks.get(name);
    }

    //todo remove this
    @Override
    protected Map<Side, FaceConfig> setFaceControl() {
        Map<Side, FaceConfig> topMap = new EnumMap<>(Side.class);
        for (Side side : Side.values()) {
            if (side != Side.UP) {
                topMap.put(side, FaceConfig.NONE);
            } else {
                topMap.put(side, FaceConfig.ENABLED);
            }
        }
        return topMap;
    }

    public void setBlockBreakProgress(ClientWorld self, WorldRenderer levelRenderer, int playerEntityId, BlockPos pos, int progress){
        BlockPos masterPos = pos.offset(this.getMasterOffset().get());
        levelRenderer.destroyBlockProgress(masterPos.hashCode(), masterPos, progress);
    }
}
