package software.bernie.techarium.display.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import software.bernie.techarium.machine.controller.MachineController;
import software.bernie.techarium.recipe.recipe.ExchangeStationRecipe;
import software.bernie.techarium.registry.ContainerRegistry;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;

public class ExchangeStationContainer extends AutomaticContainer {
    public ExchangeStationContainer(ExchangeStationTile tile, PlayerInventory inv, int id, ITextComponent containerName) {
        super(ContainerRegistry.EXCHANGE_STATION_CONTAINER.get(),tile, inv, id, containerName);
    }

    public ExchangeStationContainer(int id, PlayerInventory inv, PacketBuffer packetBuffer) {
        super(id, inv, packetBuffer);
    }
    @Override
    public MachineController<ExchangeStationRecipe> getMachineController() {
        return (MachineController<ExchangeStationRecipe>) tile.getController();
    }
}
