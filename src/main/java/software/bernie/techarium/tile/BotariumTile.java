package software.bernie.techarium.tile;

import net.minecraft.block.CropsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.block.SpecialAnimationController;
import software.bernie.geckolib.entity.IAnimatable;
import software.bernie.geckolib.event.predicate.SpecialAnimationPredicate;
import software.bernie.geckolib.manager.AnimationManager;
import software.bernie.techarium.client.screen.draw.IDrawable;
import software.bernie.techarium.item.UpgradeItem;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.machine.addon.inventory.DrawableInventoryAddon;
import software.bernie.techarium.machine.addon.inventory.InventoryAddon;
import software.bernie.techarium.machine.addon.progressbar.ProgressBarAddon;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.tile.base.MachineMasterTile;

import java.util.HashMap;
import java.util.Map;

import static software.bernie.techarium.client.screen.draw.GuiAddonTextures.*;
import static software.bernie.techarium.registry.BlockTileRegistry.BOTARIUM;

public class BotariumTile extends MachineMasterTile implements IAnimatable
{
    private final int sizeX = 172;
    private final int sizeY = 184;
    private AnimationManager manager = new AnimationManager();
    private SpecialAnimationController controller = new SpecialAnimationController(this, "controller", 0, this::animationPredicate);

    public boolean isOpening = false;

    private <E extends IAnimatable> boolean animationPredicate(SpecialAnimationPredicate<E> event)
    {
        if(isOpening)
        {
            this.controller.setAnimation(new AnimationBuilder().addAnimation("Botarium.anim.deploy", false).addAnimation("Botarium.anim.idle", true));
        }
        else {
            this.controller.setAnimation(new AnimationBuilder().addAnimation("Botarium.anim.idle", true));
        }
        return true;
    }

    public BotariumTile() {
        super(BOTARIUM.getTileEntityType());
        getController().addController(machineController(1, BOTARIUM_BASE_TIER_1));
        getController().addController(machineController(2, BOTARIUM_BASE_TIER_2));
        getController().addController(machineController(3, BOTARIUM_BASE_TIER_3));
        getController().addController(machineController(4, BOTARIUM_BASE_TIER_4));
        getController().addController(machineController(5, BOTARIUM_BASE_TIER_5));
        this.manager.addAnimationController(controller);
    }


    private MachineController machineController(int tier, IDrawable background) {
        MachineController controller = createController(tier);
        controller.setBackground(background, sizeX, sizeY);
        controller.setPowered(true);
        controller.setEnergyStorage(10000, 10000, 8, 35);

        controller.addProgressBar(new ProgressBarAddon(this,8,26,500,"techarium.gui.mainProgress"));

        controller.addTank(new FluidTankAddon(this, "waterIn", 10000 * tier, 29, 28));
        controller.addInventory(new InventoryAddon(this, "cropInput", 49, 35, 1)
                .setInputFilter((itemStack, integer) -> itemStack.getItem() instanceof BlockItem &&
                        ((BlockItem) itemStack.getItem()).getBlock() instanceof CropsBlock
                )
        );
        controller.addInventory(new InventoryAddon(this, "soilInput", 49, 67, 1)
                .setInputFilter((itemStack, integer) -> itemStack.getItem().equals(Items.DIRT)));


        controller.addInventory(new InventoryAddon(this, "upgradeSlot", 83, 81, 1 + (tier - 1))
                .setInputFilter((itemStack, integer) -> itemStack.getItem() instanceof UpgradeItem));

        controller.addInventory(new DrawableInventoryAddon(this, "output", 183, 49, BOTARIUM_OUTPUT_SLOT, 178, 34, 30, 46, 1)
                .setInputFilter((itemStack, integer) -> false));
        return controller;
    }


    @Override
    protected Map<Side, FaceConfig> setFaceControl() {
        Map<Side, FaceConfig> faceMap = new HashMap<>();
        for (Side side : Side.values()) {
            if (side == Side.FRONT || side == Side.UP) {
                faceMap.put(side, FaceConfig.NONE);
            } else {
                faceMap.put(side, FaceConfig.ENABLED);
            }
        }
        return faceMap;
    }

    private MachineController createController(int tier) {
        return new MachineController(this, () -> this.pos, tier);
    }

    @Override
    public AnimationManager getAnimationManager()
    {
        return this.manager;
    }
}
