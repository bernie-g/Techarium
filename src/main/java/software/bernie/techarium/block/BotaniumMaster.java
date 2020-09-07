package software.bernie.techarium.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.BotaniumTile;

public class BotaniumMaster extends MachineBlock<BotaniumTile> {

    public BotaniumMaster() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5f).harvestLevel(2).harvestTool(ToolType.PICKAXE).notSolid(), BotaniumTile::new);
    }

}
