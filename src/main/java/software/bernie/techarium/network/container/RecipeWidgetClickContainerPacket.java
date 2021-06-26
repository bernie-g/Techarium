package software.bernie.techarium.network.container;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.techarium.display.container.ExchangeStationContainer;
import software.bernie.techarium.display.container.PipeContainer;
import software.bernie.techarium.pipe.util.PipeMainConfig;
import software.bernie.techarium.recipe.AbstractMachineRecipe;
import software.bernie.techarium.recipe.recipe.ExchangeStationRecipe;
import software.bernie.techarium.tile.exchangestation.ExchangeStationTile;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeWidgetClickContainerPacket extends ClientToServerContainerPacket<RecipeWidgetClickContainerPacket> {

    int recipeID;

    public RecipeWidgetClickContainerPacket() {
        super(RecipeWidgetClickContainerPacket::new);
    }

    public RecipeWidgetClickContainerPacket(ExchangeStationContainer container, int recipeID) {
        super(container);
        this.recipeID = recipeID;
    }

    public RecipeWidgetClickContainerPacket(PacketBuffer buffer) {
        super(buffer);
        recipeID = buffer.readInt();
    }

    @Override
    public boolean isValid(NetworkEvent.Context context) {
        return super.isValid(context) && getContainer(context).get() instanceof ExchangeStationContainer;
    }

    @Override
    public void write(PacketBuffer writeInto) {
        super.write(writeInto);
        writeInto.writeInt(recipeID);
    }

    @Override
    public void doAction(NetworkEvent.Context context) {
        getContainer(context).ifPresent(tempContainer -> {
            if (!(tempContainer instanceof ExchangeStationContainer))
                return;
            ExchangeStationContainer container = (ExchangeStationContainer) tempContainer;
            List<ExchangeStationRecipe> recipes = container.getMachineController().getRecipes().collect(Collectors.toList());
            if (recipes.size() < recipeID)
                return;
            container.getMachineController().setCurrentRecipe(recipes.get(recipeID));
            ExchangeStationTile tile = (ExchangeStationTile)container.getTile();
            tile.updateOutput(tile.getController().getMultiInventory().getInventoryByName("input").get().getStackInSlot(0), 0);
        });
    }
}
