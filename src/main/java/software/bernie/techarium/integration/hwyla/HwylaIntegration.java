package software.bernie.techarium.integration.hwyla;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import software.bernie.techarium.block.base.MachineBlock;

@WailaPlugin
public class HwylaIntegration implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(HwylaTooltipRenderer.INSTANCE, TooltipPosition.BODY, MachineBlock.class);
    }

}
