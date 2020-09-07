package software.bernie.techariumbotanica.tile;

import software.bernie.techariumbotanica.client.screen.draw.IDrawable;
import software.bernie.techariumbotanica.machine.controller.MachineController;
import software.bernie.techariumbotanica.tile.base.MachineTile;

import static software.bernie.techariumbotanica.client.screen.draw.GuiAddonTextures.*;
import static software.bernie.techariumbotanica.registry.BlockTileRegistry.BOTANIUM;

public class BotaniumTile extends MachineTile {

    private final int sizeX = 172;
    private final int sizeY = 184;

    public BotaniumTile() {
        super(BOTANIUM.getTileEntityType());
        getController().addController(machineController(1,BOTARIUM_BASE_TIER_1));
        getController().addController(machineController(2,BOTARIUM_BASE_TIER_2));
        getController().addController(machineController(3,BOTARIUM_BASE_TIER_3));
        getController().addController(machineController(4,BOTARIUM_BASE_TIER_4));
        getController().addController(machineController(5,BOTARIUM_BASE_TIER_5));
    }



    private MachineController machineController(int tier, IDrawable background){
        MachineController controller1 = createController(tier);
        controller1.setBackground(background,sizeX,sizeY);
        controller1.setPowered(true);
        controller1.setEnergyStorage(10000,10000,8,35);
        return controller1;
    }


    private MachineController createController(int tier){
        return new MachineController(this,()->this.pos,tier);
    }
}
