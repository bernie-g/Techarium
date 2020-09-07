package software.bernie.techariumbotanica.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import software.bernie.techariumbotanica.block.base.MachineBlock;
import software.bernie.techariumbotanica.tile.BotaniumTile;

public class BotaniumMaster extends MachineBlock<BotaniumTile> {

    public BotaniumMaster() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5f).harvestLevel(2).harvestTool(ToolType.PICKAXE).notSolid(), BotaniumTile::new);
    }

}
