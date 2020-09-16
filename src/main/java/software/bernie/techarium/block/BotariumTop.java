package software.bernie.techarium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.block.base.RotatableBlock;
import software.bernie.techarium.tile.BotariumTile;
import software.bernie.techarium.tile.slaves.TopEnabledOnlySlave;

public class BotariumTop extends MachineBlock<TopEnabledOnlySlave> {
    public BotariumTop() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5f).notSolid().noDrops(),TopEnabledOnlySlave::new);
    }



}
