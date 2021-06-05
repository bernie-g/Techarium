package software.bernie.techarium.network;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.techarium.integration.ModIntegrations;
import software.bernie.techarium.machine.addon.fluid.FluidTankAddon;
import software.bernie.techarium.machine.addon.fluid.MultiFluidTankAddon;
import software.bernie.techarium.machine.container.AutomaticContainer;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class FluidTankClickContainerPacket extends ClientToServerContainerPacket<FluidTankClickContainerPacket> {

    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int MIDDLE = 2;

    int button;
    boolean shift;
    int fluidSlot;

    //Empty dummy constructor
    public FluidTankClickContainerPacket() {
        super(FluidTankClickContainerPacket::new);
    }

    public FluidTankClickContainerPacket(AutomaticContainer container, int button, boolean shift, int fluidSlot) {
        super(container);
        this.button = button;
        this.shift = shift;
        this.fluidSlot = fluidSlot;
    }

    FluidTankClickContainerPacket(PacketBuffer readFrom) {
        super(readFrom);
        button = readFrom.readInt();
        shift = readFrom.readBoolean();
        fluidSlot = readFrom.readInt();
    }

    @Override
    void write(PacketBuffer writeInto) {
        super.write(writeInto);
        writeInto.writeInt(button);
        writeInto.writeBoolean(shift);
        writeInto.writeInt(fluidSlot);
    }

    @Override
    void doAction(NetworkEvent.Context context) {
        getContainer(context).ifPresent(container -> {
            MultiFluidTankAddon multiTank = container.getMachineController().getMultiTank();
            if (multiTank.getFluidTanks().size() > fluidSlot && fluidSlot >= 0) {
                FluidTankAddon singleTank = multiTank.getFluidTanks().get(fluidSlot);
                ItemStack stack = context.getSender().inventory.getCarried();
                if (stack.getItem() instanceof BucketItem) {
                    if (((BucketItem)stack.getItem()).getFluid() == Fluids.EMPTY) {
                        if (singleTank.drain(1000, FluidAction.SIMULATE).getAmount() == 1000) {
                            ItemStack filledBucket = new ItemStack(singleTank.getFluid().getFluid().getBucket());
                            if (stack.getCount() == 1) {
                                context.getSender().inventory.setCarried(filledBucket);
                                context.getSender().broadcastCarriedItem();
                            } else {
                                stack.shrink(1);
                                context.getSender().broadcastCarriedItem();
                                if (!context.getSender().inventory.add(filledBucket)) {
                                    //cant add item, because inventory is full
                                    context.getSender().drop(filledBucket, false, true);
                                }
                            }
                            singleTank.drain(1000, FluidAction.EXECUTE);
                        }
                    } else if (singleTank.fill(new FluidStack(((BucketItem)stack.getItem()).getFluid(), 1000), FluidAction.SIMULATE) == 1000) {
                        singleTank.fill(new FluidStack(((BucketItem)stack.getItem()).getFluid(), 1000), FluidAction.EXECUTE);
                        context.getSender().inventory.setCarried(new ItemStack(Items.BUCKET));
                        context.getSender().broadcastCarriedItem();
                    }
                } else if (ModIntegrations.getMekanism().isPresent() && ModIntegrations.getMekanism().orElseThrow(NullPointerException::new).isGauge(stack.getItem())) {
                    ModIntegrations.getMekanism().ifPresent(mekanismIntegration -> {
                        if (button == LEFT) {
                            if (shift) {
                                singleTank.setFluid(FluidStack.EMPTY);
                            } else {
                                FluidStack tank = singleTank.getFluid().copy();
                                tank.setAmount(Math.min(singleTank.getFluidAmount(), 16000));
                                int filled = mekanismIntegration.fillGauge(stack, tank);
                                singleTank.drain(filled, FluidAction.EXECUTE);
                            }
                        } else if (button == RIGHT) {
                            FluidStack gauge = mekanismIntegration.readFluidStackFromGauge(stack);
                            int filled = singleTank.fill(gauge, FluidAction.EXECUTE);
                            mekanismIntegration.drainGauge(filled, stack);
                        }
                        context.getSender().broadcastCarriedItem();
                    });
                } else {
                    LazyOptional<IFluidHandlerItem> fluidCapability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
                    fluidCapability.ifPresent(iFluidHandlerItem -> {
                        if (button == LEFT) {
                            int filled = iFluidHandlerItem.fill(singleTank.drain(Integer.MAX_VALUE, FluidAction.SIMULATE), FluidAction.EXECUTE);
                            singleTank.drain(filled, FluidAction.EXECUTE);
                        } else if (button == RIGHT) {
                            int filled = singleTank.fill(iFluidHandlerItem.drain(Integer.MAX_VALUE, FluidAction.SIMULATE), FluidAction.EXECUTE);
                            iFluidHandlerItem.drain(filled, FluidAction.EXECUTE);
                        }
                        context.getSender().broadcastCarriedItem();
                    });
                }
            }
        });
    }
}
