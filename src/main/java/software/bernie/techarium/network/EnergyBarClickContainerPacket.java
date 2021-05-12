package software.bernie.techarium.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.techarium.machine.addon.energy.EnergyStorageAddon;
import software.bernie.techarium.machine.container.AutomaticContainer;

public class EnergyBarClickContainerPacket extends ClientToServerContainerPacket<EnergyBarClickContainerPacket> {
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int MIDDLE = 2;
    int button;
    public EnergyBarClickContainerPacket() {
        super(EnergyBarClickContainerPacket::new);
    }

    public EnergyBarClickContainerPacket(AutomaticContainer container, int button) {
        super(container);
        this.button = button;
    }

    public EnergyBarClickContainerPacket(PacketBuffer buffer) {
        super(buffer);
        button = buffer.readInt();
    }

    @Override
    void write(PacketBuffer writeInto) {
        super.write(writeInto);
        writeInto.writeInt(button);
    }

    @Override
    void doAction(NetworkEvent.Context context) {
        getContainer(context).ifPresent(container -> {
            ItemStack stack = context.getSender().inventory.getItemStack();
            LazyOptional<IEnergyStorage> energy = stack.getCapability(CapabilityEnergy.ENERGY);

            energy.ifPresent(energyItem -> {
                EnergyStorageAddon addon = container.getMachineController().getEnergyStorage();
                if (button == LEFT) {
                    int received = energyItem.receiveEnergy(addon.extractEnergy(Integer.MAX_VALUE, true), false);
                    addon.extractEnergy(received, false);
                } else if (button == RIGHT) {
                    int received = addon.receiveEnergy(energyItem.extractEnergy(Integer.MAX_VALUE, true), false);
                    energyItem.extractEnergy(received, false);
                }
                context.getSender().updateHeldItem();
            });
        });
    }
}
