package software.bernie.techarium.tile;

import net.minecraft.block.CropsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.item.UpgradeItem;
import software.bernie.techarium.machine.addon.inventory.DrawableInventoryAddon;
import software.bernie.techarium.machine.addon.inventory.InventoryAddon;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.tile.base.MachineMasterTile;

import java.util.HashMap;
import java.util.Map;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.*;
import static software.bernie.techarium.registry.BlockTileRegistry.BOTANIUM;

public class BotaniumTile extends MachineMasterTile {

    private final int sizeX = 172;
    private final int sizeY = 184;

    public BotaniumTile() {
        super(BOTANIUM.getTileEntityType());
        getController().addController(machineController(1, BOTARIUM_BASE_TIER_1));
        getController().addController(machineController(2, BOTARIUM_BASE_TIER_2));
        getController().addController(machineController(3, BOTARIUM_BASE_TIER_3));
        getController().addController(machineController(4, BOTARIUM_BASE_TIER_4));
        getController().addController(machineController(5, BOTARIUM_BASE_TIER_5));
    }


    private MachineController machineController(int tier, IDrawable background) {
        MachineController controller1 = createController(tier);
        controller1.setBackground(background, sizeX, sizeY);
        controller1.setPowered(true);
        controller1.setEnergyStorage(10000, 10000, 8, 35);
        controller1.addInventory(new InventoryAddon(this, "cropInput", 48, 34, 1)
                .setInputFilter((itemStack, integer) -> itemStack.getItem() instanceof BlockItem &&
                        ((BlockItem) itemStack.getItem()).getBlock() instanceof CropsBlock
                )
        );
        controller1.addInventory(new InventoryAddon(this, "soilInput", 48, 66, 1)
        .setInputFilter((itemStack, integer) -> itemStack.getItem().equals(Items.DIRT)));


        controller1.addInventory(new InventoryAddon(this, "upgradeSlot", 83, 81, 1 + (tier - 1))
                .setInputFilter((itemStack, integer) -> itemStack.getItem() instanceof UpgradeItem));

        controller1.addInventory(new DrawableInventoryAddon(this, "output", 183, 49,BOTARIUM_OUTPUT_SLOT,178,34,30,46,1)
                .setInputFilter((itemStack, integer) -> false));
        return controller1;
    }


    @Override
    protected Map<Side, FaceConfig> setFaceControl() {
        Map<Side, FaceConfig> faceMap = new HashMap<>();
        for (Side side : Side.values()) {
            if (side == Side.FRONT || side == Side.UP) {
                faceMap.put(side, FaceConfig.NONE);
            } else{
                faceMap.put(side, FaceConfig.ENABLED);
            }
        }
        return faceMap;
    }

    private MachineController createController(int tier) {
        return new MachineController(this, () -> this.pos, tier);
    }

}
