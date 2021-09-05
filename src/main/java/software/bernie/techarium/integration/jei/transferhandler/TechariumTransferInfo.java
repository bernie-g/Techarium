package software.bernie.techarium.integration.jei.transferhandler;

import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import software.bernie.techarium.display.container.AutomaticContainer;
import software.bernie.techarium.integration.jei.category.BaseRecipeCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TechariumTransferInfo implements IRecipeTransferInfo<AutomaticContainer> {

    public static final Map<ResourceLocation, TechariumTransferInfo> TRANSFER_INFOS = new HashMap();

    private final BaseRecipeCategory category;
    private final int recipeSlotStart;
    private final int recipeSlotCount;
    private final int inventorySlotStart;
    private final int inventorySlotCount;

    public TechariumTransferInfo(BaseRecipeCategory category, int recipeSlotStart, int recipeSlotCount, int inventorySlotStart, int inventorySlotCount) {
        TRANSFER_INFOS.put(category.getUid(), this);
        this.category = category;
        this.recipeSlotStart = recipeSlotStart;
        this.recipeSlotCount = recipeSlotCount;
        this.inventorySlotStart = inventorySlotStart;
        this.inventorySlotCount = inventorySlotCount;
    }

    @Override
    public Class<AutomaticContainer> getContainerClass() {
        return AutomaticContainer.class;
    }

    @Override
    public ResourceLocation getRecipeCategoryUid() {
        return category.getUid();
    }

    @Override
    public boolean canHandle(AutomaticContainer container) {
        return container.getTile().getRecipeClass() == category.getRecipeClass();
    }

    @Override
    public List<Slot> getRecipeSlots(AutomaticContainer container) {
        List<Slot> slots = new ArrayList<>();
        for (int i = recipeSlotStart; i < recipeSlotStart + recipeSlotCount; i++) {
            Slot slot = container.getSlot(i);
            slots.add(slot);
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(AutomaticContainer container) {
        List<Slot> slots = new ArrayList<>();
        for (int i = inventorySlotStart; i < inventorySlotStart + inventorySlotCount; i++) {
            Slot slot = container.getSlot(i);
            slots.add(slot);
        }
        return slots;
    }
}
