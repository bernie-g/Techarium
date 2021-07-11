package software.bernie.techarium.block.botarium;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import software.bernie.techarium.block.slave.SlaveBlock;
import software.bernie.techarium.registry.BlockRegistry;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class BotariumTop extends SlaveBlock {

    public BotariumTop() {
        super(BlockBehaviours.botarium);
    }


    @Override
    public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(BlockRegistry.BOTARIUM.getItem());
    }
}
