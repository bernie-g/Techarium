package software.bernie.techarium.integration.theoneprobe;

import mcjty.theoneprobe.api.*;
import net.minecraftforge.fml.InterModComms;
import software.bernie.techarium.integration.Integration;

import java.util.function.Function;

public class TheOneProbeIntegration extends Integration implements Function<ITheOneProbe, Void> {

    public static ITheOneProbe probe;

    public void requestTheOneProbe() {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", () -> this);
    }

    @Override
    public Void apply(ITheOneProbe iTheOneProbe) {
        probe = iTheOneProbe;
        probe.registerProvider(new ProbeInfoProvider());

        return null;
    }
}
