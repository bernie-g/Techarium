package software.bernie.techarium.item;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.base.MachineMasterTile;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUMS;

public class PowerStickDebug extends Item {

    public PowerStickDebug() {
        super(new Item.Properties().tab(TECHARIUMS));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(world.getBlockState(pos).getBlock() instanceof MachineBlock<?>){
           TileEntity tile = world.getBlockEntity(pos);
           if(tile instanceof MachineMasterTile){
               MachineMasterTile<?> macTile = (MachineMasterTile<?>) tile;
               if(macTile.isPowered()) {
                   macTile.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> energy.receiveEnergy(energy.getMaxEnergyStored()/10,false));
                   macTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(fluid -> fluid.fill(new FluidStack(Fluids.WATER,1500), IFluidHandler.FluidAction.EXECUTE));

               }
           }
        }

        return super.useOn(context);
    }
}
