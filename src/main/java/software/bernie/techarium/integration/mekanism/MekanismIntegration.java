package software.bernie.techarium.integration.mekanism;

import mekanism.common.registries.MekanismItems;
import net.minecraft.item.Item;
import software.bernie.techarium.integration.Integration;

/**
 * Anything in this class is safe to use without worrying about ClassNotfound's, the Wrapper takes care of the overhead
 */
public class MekanismIntegration extends Integration {
    public MekanismIntegration(String modID) {
        super(modID);
    }

    public boolean isGauge(Item item)
    {
        return item == MekanismItems.GAUGE_DROPPER.getItem();
    }
}
