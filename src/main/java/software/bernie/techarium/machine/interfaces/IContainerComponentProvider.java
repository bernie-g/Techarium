package software.bernie.techarium.machine.interfaces;

import net.minecraft.inventory.container.Slot;

import java.util.List;

public interface IContainerComponentProvider {

    List<IFactory<? extends Slot>> getContainerComponents();

}
