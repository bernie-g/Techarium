package software.bernie.techarium.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.base.MachineTile;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUMS;

public class PowerStickDebug extends Item {

    public PowerStickDebug() {
        super(new Item.Properties().group(TECHARIUMS));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        if(world.getBlockState(pos).getBlock() instanceof MachineBlock<?>){
           TileEntity tile = world.getTileEntity(pos);
           if(tile instanceof MachineTile){
               MachineTile macTile = (MachineTile) tile;
               if(macTile.isPowered()) {
                   macTile.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> energy.receiveEnergy(energy.getMaxEnergyStored()/10,false));
               }
           }
        }

        return super.onItemUse(context);
    }
}
