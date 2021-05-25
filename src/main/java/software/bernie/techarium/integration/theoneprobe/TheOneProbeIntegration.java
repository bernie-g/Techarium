package software.bernie.techarium.integration.theoneprobe;

import mcjty.theoneprobe.api.*;
import net.minecraftforge.fml.InterModComms;
import software.bernie.techarium.integration.Integration;

import java.util.function.Function;

public class TheOneProbeIntegration extends Integration {

    public TheOneProbeIntegration(String modID) {
        super(modID);
    }

    public void requestTheOneProbe() {
        new ProbeHandler().requestTheOneProbe();
    }


    private static class ProbeHandler implements Function<ITheOneProbe, Void> {

        void requestTheOneProbe() {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", () -> this);
        }

        @Override
        public Void apply(ITheOneProbe iTheOneProbe) {
            iTheOneProbe.registerProvider(new ProbeInfoProvider());
            return null;
        }
    }
}
